import { useState, useEffect } from 'react'
import Taro from '@tarojs/taro'
import { Popup, Input, Button } from '@nutui/nutui-react-taro'
import { userApi } from '@/api'
import { setToken } from '@/utils/auth'

const btnPrimaryStyle = {
  background: 'var(--theme-primary)',
  borderColor: 'var(--theme-primary)',
}

interface Props {
  onSuccess: (token: string) => void
  onCancel: () => void
}

/** 验证码倒计时秒数 */
const COUNTDOWN_SECONDS = 60

export default function PhoneLoginModal({ onSuccess, onCancel }: Props) {
  const [visible, setVisible] = useState(true)
  const [phone, setPhone] = useState('')
  const [code, setCode] = useState('')
  const [loading, setLoading] = useState(false)
  const [sendingSms, setSendingSms] = useState(false)
  const [countdown, setCountdown] = useState(0)

  useEffect(() => {
    if (countdown <= 0) return
    const timer = setInterval(() => {
      setCountdown((c) => c - 1)
    }, 1000)
    return () => clearInterval(timer)
  }, [countdown])

  const close = () => {
    setVisible(false)
    setTimeout(onCancel, 300)
  }

  const handleSendSms = async () => {
    if (sendingSms || countdown > 0) return
    if (!phone) {
      Taro.showToast({ title: '请输入手机号', icon: 'none' })
      return
    }
    setSendingSms(true)
    try {
      await userApi.sendSms({ phone })
      setCountdown(COUNTDOWN_SECONDS)
      Taro.showToast({ title: '验证码已发送', icon: 'none' })
    } catch (err: any) {
      Taro.showToast({ title: err?.message || '发送失败', icon: 'none' })
    } finally {
      setSendingSms(false)
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
      const token = res?.token
      console.log(res);
      if (token) {
        setToken(token)
        setVisible(false)
        setTimeout(() => onSuccess(token), 300)
      } else {
        Taro.showToast({ title: '登录失败', icon: 'none' })
      }
    } catch (err: any) {
      Taro.showToast({ title: err?.message || '登录失败', icon: 'none' })
    } finally {
      // setLoading(false)
    }
  }

  return (
    <Popup
      visible={visible}
      onClose={close}
      closeable
      position="center"
      round
      style={{ width: 340 }}
    >
      <div style={{ padding: 28, boxSizing: 'border-box' }}>
        <h3 style={{
          margin: '0 0 24px',
          fontSize: 18,
          textAlign: 'center',
          fontWeight: 600,
          color: 'var(--theme-text)',
        }}>
          手机号登录
        </h3>

        <div style={{ marginBottom: 16 }}>
          <Input
            placeholder="请输入手机号"
            type="tel"
            value={phone}
            onChange={(val: string) => setPhone(val)}
          />
        </div>

        <div style={{ display: 'flex', gap: 10, marginBottom: 24, alignItems: 'center' }}>
          <div style={{ flex: 1 }}>
            <Input
              placeholder="请输入验证码"
              type="number"
              value={code}
              onChange={(val: string) => setCode(val)}
            />
          </div>
          <Button
            size="small"
            // disabled={countdown > 0}
            onClick={handleSendSms}
            style={{
              ...btnPrimaryStyle,
              color: '#fff',
              borderRadius: 8,
              height: 40,
              width: 90,
              flexShrink: 0,
              opacity: sendingSms || countdown > 0 ? 0.5 : 1,
            }}
          >
            {countdown > 0 ? `${countdown}S后重发` : '获取验证码'}
          </Button>
        </div>

        <Button
          type="primary"
          block
          // loading={loading}
          onClick={handleLogin}
          style={{ ...btnPrimaryStyle, borderRadius: 8, height: 44 }}
        >
          登录
        </Button>
      </div>
    </Popup>
  )
}
