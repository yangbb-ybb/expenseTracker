import { useState } from 'react'
import Taro from '@tarojs/taro'
import { Popup, Input, Button } from '@nutui/nutui-react-taro'
import { userApi } from '@/api'
import { setToken } from '@/utils/auth'

interface Props {
  onSuccess: (token: string) => void
  onCancel: () => void
}

export default function PhoneLoginModal({ onSuccess, onCancel }: Props) {
  const [visible, setVisible] = useState(true)
  const [phone, setPhone] = useState('')
  const [code, setCode] = useState('')
  const [loading, setLoading] = useState(false)

  const close = () => {
    setVisible(false)
    setTimeout(onCancel, 300)
  }

  const handleSendSms = async () => {
    if (!phone) {
      Taro.showToast({ title: '请输入手机号', icon: 'none' })
      return
    }
    try {
      await userApi.sendSms({ phone })
      Taro.showToast({ title: '验证码已发送', icon: 'none' })
    } catch {
      Taro.showToast({ title: '发送失败', icon: 'none' })
    }
  }

  const handleLogin = async () => {
    if (!phone || !code) {
      Taro.showToast({ title: '请填写完整信息', icon: 'none' })
      return
    }
    setLoading(true)
    try {
      const res: any = await userApi.smsLogin({ phone, code })
      const token = res?.token ?? res?.data?.token
      if (token) {
        setToken(token)
        setVisible(false)
        setTimeout(() => onSuccess(token), 300)
      } else {
        Taro.showToast({ title: '登录失败', icon: 'none' })
      }
    } catch {
      Taro.showToast({ title: '登录失败', icon: 'none' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <Popup
      visible={visible}
      onClose={close}
      closeable
      position="center"
    >
      <div style={{ padding: 28, width: '80vw', maxWidth: 360, minWidth: 280, boxSizing: 'border-box' }}>
        <h3 style={{ margin: '0 0 24px', fontSize: 18, textAlign: 'center', fontWeight: 600 }}>
          手机号登录
        </h3>
        <div style={{ marginBottom: 16, border: '1px solid #e8e8e8', borderRadius: 8, padding: '4px 8px' }}>
          <Input
            placeholder="请输入手机号"
            type="tel"
            value={phone}
            onChange={(val: string) => setPhone(val)}
          />
        </div>
        <div style={{ display: 'flex', gap: 12, marginBottom: 16 }}>
          <div style={{ flex: 1, border: '1px solid #e8e8e8', borderRadius: 8, padding: '4px 8px' }}>
            <Input
              placeholder="请输入验证码"
              type="number"
              value={code}
              onChange={(val: string) => setCode(val)}
            />
          </div>
          <Button
            type="primary"
            size="small"
            onClick={handleSendSms}
            style={{ height: 40, alignSelf: 'center' }}
          >
            获取验证码
          </Button>
        </div>
        <Button
          type="primary"
          block
          loading={loading}
          onClick={handleLogin}
        >
          登录
        </Button>
      </div>
    </Popup>
  )
}
