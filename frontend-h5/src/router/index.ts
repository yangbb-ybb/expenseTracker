import { useNavigate, useLocation } from 'react-router-dom'

// 封装 hooks
export const useRouter = () => {
  const navigate = useNavigate()
  const location = useLocation()

  return {
    navigate,
    location,
    // 跳转到指定页面
    push: (path: string) => navigate(path),
    // 返回上一页
    back: () => navigate(-1),
    // 替换当前页
    replace: (path: string) => navigate(path, { replace: true })
  }
}

// 路径配置（可选）
export const routes = {
  home: '/pages/index/index',
  demo: '/pages/demo/demo'
}
