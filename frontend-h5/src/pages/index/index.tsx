import { View, Text } from '@tarojs/components'
import { ConfigProvider } from '@nutui/nutui-react-taro'
import { Edit } from '@nutui/icons-react-taro'
import Taro from '@tarojs/taro'
import { useState } from 'react'
import { consumeApi } from '@/api/consume'
import UserInfo from './components/UserInfo'
import TransactionList from './components/TransactionList'
import AddRecordPopup from './components/AddRecordPopup'
import './index.scss'

export default function Index() {
  const [showPopup, setShowPopup] = useState(false)

  const handleAddRecord = () => {
    setShowPopup(true)
  }

  const handleConfirm = (data: {
    type: 'income' | 'expense'
    category: string
    amount: number
    description?: string
    date?: string
    createTime?: string
  }) => {
    // 调用 API 保存记录
    consumeApi.create(data).then(() => {
      Taro.showToast({ title: '添加成功', icon: 'success' })

      // 刷新列表
      Taro.eventCenter.trigger('refreshTransactionList')
    }).catch((error: any) => {
      console.error('保存失败:', error)
      Taro.showToast({ title: '保存失败', icon: 'none' })
    }).finally(() => {
      // 关闭弹窗
      setShowPopup(false)
    })
  }

  return (
    <ConfigProvider>
      <View className='page'>
        <UserInfo />
        <TransactionList />
        <View className='fab-btn' onClick={handleAddRecord}>
          <Edit size={16} color='var(--theme-primary)' />
          <Text>记一笔</Text>
        </View>

        <AddRecordPopup
          visible={showPopup}
          onClose={() => setShowPopup(false)}
          onConfirm={handleConfirm}
        />
      </View>
    </ConfigProvider>
  )
}
