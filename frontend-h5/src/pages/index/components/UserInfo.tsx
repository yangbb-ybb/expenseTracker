import { View, Text } from '@tarojs/components'
import { Avatar } from '@nutui/nutui-react-taro'
import { useEffect, useState } from 'react'
import { userApi } from '@/api'
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

  useEffect(() => {
    async function init() {
      try {
        await ensureLoggedIn()
        const res: any = await userApi.getUserInfo()
        console.log(res);
        if (res?.id) {
          setUserInfo({
            username: res.username || '用户',
            avatar: res.avatar || '',
            balance: res.balance || '0.00',
            id: res.id || ''
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
        <Text className='user-info__balance-label'>总支出</Text>
        <Text className='user-info__balance-value'>¥ {userInfo.balance}</Text>
      </View>
    </View>
  )
}
