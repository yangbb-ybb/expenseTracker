import { PropsWithChildren, useEffect } from 'react'
import '@nutui/nutui-react-taro/dist/style.css'
import DynamicTabBar from './components/DynamicTabBar'
import './app.scss'

function App({ children }: PropsWithChildren) {
  useEffect(() => {
    // 设置 rem 基准值
    const baseSize = 37.5
    const updateRem = () => {
      const scale = window.innerWidth / 375
      document.documentElement.style.fontSize = `${baseSize * scale}px`
    }
    updateRem()
    window.addEventListener('resize', updateRem)
    return () => window.removeEventListener('resize', updateRem)
  }, [])

  return (
    <>
      {children}
      <DynamicTabBar />
    </>
  )
}

export default App
