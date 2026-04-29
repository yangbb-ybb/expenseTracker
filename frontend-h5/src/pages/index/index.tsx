import { View } from '@tarojs/components'
import { ConfigProvider } from '@nutui/nutui-react-taro'
import UserInfo from './components/UserInfo'
import TransactionList from './components/TransactionList'
import './index.scss'

export default function Index() {
  return (
    <ConfigProvider>
      <View className='page'>
        <UserInfo username='张三' balance='1,288.50' />
        <TransactionList />
      </View>
    </ConfigProvider>
  )
}
