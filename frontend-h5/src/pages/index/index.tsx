import { View, Text } from '@tarojs/components'
import { ConfigProvider } from '@nutui/nutui-react-taro'
import { Edit } from '@nutui/icons-react-taro'
import UserInfo from './components/UserInfo'
import TransactionList from './components/TransactionList'
import './index.scss'

export default function Index() {
  return (
    <ConfigProvider>
      <View className='page'>
        <UserInfo />
        <TransactionList />
        <View className='fab-btn'>
          <Edit size={16} color='var(--theme-primary)' />
          <Text>记一笔</Text>
        </View>
      </View>
    </ConfigProvider>
  )
}
