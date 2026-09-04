import ReactDOM from 'react-dom/client'
import PhoneLoginModal from './index'

export function showPhoneLoginModal(): Promise<string> {
  return new Promise((resolve, reject) => {
    const container = document.createElement('div')
    document.body.appendChild(container)

    const root = ReactDOM.createRoot(container)

    const cleanup = () => {
      setTimeout(() => {
        root.unmount()
        if (container.parentNode) {
          container.parentNode.removeChild(container)
        }
      }, 300)
    }

    root.render(
      <PhoneLoginModal
        onSuccess={(token) => { cleanup(); resolve(token) }}
        onCancel={() => { cleanup(); reject(new Error('用户取消登录')) }}
      />
    )
  })
}
