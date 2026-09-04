import { View, Text } from '@tarojs/components'
import { Avatar, Dialog } from '@nutui/nutui-react-taro'
import Taro from '@tarojs/taro'
import { useEffect, useState } from 'react'
import { userApi } from '@/api'
import { consumeApi } from '@/api/consume'
import { ensureLoggedIn, doLogin, isLoggedIn } from '@/utils/auth'
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
  const [switchDialogVisible, setSwitchDialogVisible] = useState(false)

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

        // 获取统计数据（传入当前月份）
        const now = new Date()
        const currentMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
        const stats: any = await consumeApi.statistics(currentMonth)
        if (stats) {
          setStatistics({
            totalExpense: (Number(stats.totalExpense || 0) / 100).toFixed(2),
            totalIncome: (Number(stats.totalIncome || 0) / 100).toFixed(2)
          })
        }
      } catch {
        setUserInfo(prev => ({ ...prev, username: '未登录' }))
      }
    }

    init()
  }, [])

  const handleLogin = async () => {
    try {
      await doLogin()
    } catch (error: any) {
      if (error?.message !== '用户取消登录') {
        console.error('登录失败:', error)
      }
    }
  }

  const handleNameClick = () => {
    if (isLoggedIn()) {
      setSwitchDialogVisible(true)
    } else {
      handleLogin()
    }
  }

  // 监听月份变化事件
  useEffect(() => {
    const handleMonthChange = (month: string) => {
      // 重新获取统计数据
      consumeApi.statistics(month).then((stats: any) => {
        if (stats) {
          setStatistics({
            totalExpense: (Number(stats.totalExpense || 0) / 100).toFixed(2),
            totalIncome: (Number(stats.totalIncome || 0) / 100).toFixed(2)
          })
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    }

    Taro.eventCenter.on('monthChanged', handleMonthChange)

    return () => {
      Taro.eventCenter.off('monthChanged', handleMonthChange)
    }
  }, [])

  return (
    <View className='user-info'>
      <View className='user-info__header'>
        <Avatar size='large' src={userInfo.avatar || ''}></Avatar>
        <View className='user-info__info'>
          <Text className='user-info__name' onClick={handleNameClick}>{maskPhone(userInfo.username)}</Text>
          {userInfo.id && <Text className='user-info__id'>ID: {userInfo.id}</Text>}
          <Dialog
            visible={switchDialogVisible}
            title='提示'
            content='切换账号需重新登录，是否继续？'
            onConfirm={() => {
              setSwitchDialogVisible(false)
              handleLogin()
            }}
            onCancel={() => setSwitchDialogVisible(false)}
            onClose={() => setSwitchDialogVisible(false)}
          />
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
