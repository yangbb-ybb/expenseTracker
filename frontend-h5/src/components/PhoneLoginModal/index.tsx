import { useState, useEffect } from 'react'
import Taro from '@tarojs/taro'
import { Input } from '@nutui/nutui-react-taro'
import { userApi } from '@/api'
import { setToken } from '@/utils/auth'
import './index.scss'

interface Props {
  onSuccess: (token: string) => void
  onCancel: () => void
}

/** 验证码倒计时秒数 */
const COUNTDOWN_SECONDS = 60

export default function PhoneLoginModal({ onSuccess, onCancel }: Props) {
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

  const handleSendSms = async () => {
    if (sendingSms || countdown > 0) return
    if (!/^1\d{10}$/.test(phone)) {
      Taro.showToast({ title: '请输入正确的手机号', icon: 'none' })
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
    if (!/^1\d{10}$/.test(phone)) {
      Taro.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return
    }
    if (!/^\d{6}$/.test(code)) {
      Taro.showToast({ title: '请输入 6 位验证码', icon: 'none' })
      return
    }
    setLoading(true)
    try {
      const res: any = await userApi.smsLogin({ phone, code })
      const token = res?.token ?? res?.data?.token
      if (token) {
        setToken(token)
        Taro.showToast({ title: '登录成功', icon: 'success' })
        setTimeout(() => onSuccess(token), 300)
      } else {
        Taro.showToast({ title: '登录失败', icon: 'none' })
      }
    } catch (err: any) {
      Taro.showToast({ title: err?.message || '登录失败', icon: 'none' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className='login-page'>
      {/* 背景水印 */}
      <div className='login-page__watermark'>huazhi · 记账</div>
      <div className='login-page__watermark login-page__watermark--alt'>aiword · 20260904</div>

      <div className='login-card'>
        {/* 顶部品牌 */}
        <div className='login-card__brand'>
          {/* <div className='login-card__logo'>记</div> */}
          {/* <span className='login-card__title'>记账</span> */}
        </div>
        {/* <p className='login-card__subtitle'>简单记录每一笔 · 智能分析每一月</p> */}

        {/* 手机号 */}
        <div className='login-card__field'>
          <label className='login-card__label'>手机号</label>
          <div className='login-card__input'>
            <Input
              placeholder='11 位手机号'
              type='tel'
              maxLength={11}
              value={phone}
              onChange={(val: string) => setPhone(val.replace(/\D/g, ''))}
            />
          </div>
        </div>

        {/* 验证码 */}
        <div className='login-card__field'>
          <label className='login-card__label'>验证码</label>
          <div className='login-card__code-row'>
            <div className='login-card__input login-card__input--code'>
              <Input
                placeholder='6 位数字'
                type='number'
                maxLength={6}
                value={code}
                onChange={(val: string) => setCode(val.replace(/\D/g, ''))}
              />
            </div>
            <div
              className={`login-card__sms-btn ${countdown > 0 || sendingSms ? 'is-disabled' : ''}`}
              onClick={handleSendSms}
            >
              {countdown > 0 ? `${countdown}s 后重发` : '获取验证码'}
            </div>
          </div>
        </div>

        {/* 登录按钮 */}
        <div
          className={`login-card__submit ${loading ? 'is-loading' : ''}`}
          onClick={handleLogin}
        >
          {loading ? '登录中...' : '登录 / 注册'}
        </div>

        {/* dev 提示 */}
        <p className='login-card__dev-hint'>dev 阶段短信直接打印到控制台，不接网关</p>
      </div>

      {/* 底部协议 */}
      <div className='login-footer'>
        <span className='login-footer__text'>登录即表示同意</span>
        <span className='login-footer__link' onClick={() => Taro.showToast({ title: '服务条款', icon: 'none' })}>服务条款</span>
        <span className='login-footer__text'> · </span>
        <span className='login-footer__link' onClick={() => Taro.showToast({ title: '隐私政策', icon: 'none' })}>隐私政策</span>
        {/* 兜底取消入口（无 UI 按钮，长按 logo 可退出调试） */}
        <span
          className='login-footer__cancel-hint'
          onDoubleClick={onCancel}
        />
      </div>
    </div>
  )
}
