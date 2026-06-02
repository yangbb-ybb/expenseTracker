import { View, Text } from '@tarojs/components'
import { Avatar } from '@nutui/nutui-react-taro'
import { useEffect, useState } from 'react'
import { userApi } from '@/api'
import { consumeApi } from '@/api/consume'
import { ensureLoggedIn } from '@/utils/auth'
import { maskPhone } from '@/utils/format'
import './UserInfo.scss'

interface UserInfoProps {
  username?: string
  avatar?: string
  balance?: string
}

export default function UserInfo({ username, avatar, balance }: UserInfoProps) {
  const [userInfo, setUserInfo] = useState({
    username: username || '用户',
    avatar: avatar || '',
    balance: balance || '0.00',
    id: ''
  })
  const [statistics, setStatistics] = useState({
    totalExpense: '0.00',
    totalIncome: '0.00'
  })

  useEffect(() => {
    async function init() {
      try {
        await ensureLoggedIn()
        const res: any = await userApi.getUserInfo()
        if (res?.id) {
          setUserInfo({
            username: res.username || '用户',
            avatar: res.avatar || '',
            balance: res.balance || '0.00',
            id: res.id || ''
          })
        }

        const stats: any = await consumeApi.statistics()
        if (stats) {
          setStatistics({
            totalExpense: Number(stats.totalExpense || 0).toFixed(2),
            totalIncome: Number(stats.totalIncome || 0).toFixed(2)
          })
        }
      } catch {
        setUserInfo(prev => ({ ...prev, username: '未登录' }))
      }
    }

    init()
  }, [])

  return (
    <View className='user-info'>
      <View className='user-info__header'>
        <Avatar size='large' src={userInfo.avatar || ''}></Avatar>
        <View className='user-info__info'>
          <Text className='user-info__name'>{maskPhone(userInfo.username)}</Text>
          {userInfo.id && <Text className='user-info__id'>ID: {userInfo.id}</Text>}
        </View>
      </View>
      <View className='user-info__balance'>
        <View className='user-info__balance-item'>
          <Text className='user-info__balance-label'>总支出</Text>
          <Text className='user-info__balance-value expense'>¥ {statistics.totalExpense}</Text>
        </View>
        <View className='user-info__balance-item'>
          <Text className='user-info__balance-label'>总收入</Text>
          <Text className='user-info__balance-value income'>¥ {statistics.totalIncome}</Text>
        </View>
      </View>
    </View>
  )
}
