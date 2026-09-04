import { View, Text, ScrollView } from '@tarojs/components'
import { Cell, Loading, DatePicker } from '@nutui/nutui-react-taro'
import Taro from '@tarojs/taro'
import { useState, useEffect } from 'react'
import { consumeApi } from '@/api/consume'
import './TransactionList.scss'

interface Transaction {
  id: number
  title: string
  time: string
  amount: string
  type: 'income' | 'expense'
}

interface TransactionListProps {
  list?: Transaction[]
}

interface TransactionRecord {
  id: number
  userId: number
  date: string
  consumeType: string
  category: string
  consumeAmount: number
  createTime: string
  consumeCategory: string
  description: string
}

export default function TransactionList({ list = [] }: TransactionListProps) {
  // 分页状态
  const [page, setPage] = useState(1)
  const pageSize = 10
  const [loading, setLoading] = useState(false)
  const [allList, setAllList] = useState<Transaction[]>([])
  const [hasMore, setHasMore] = useState(true)
  const [currentMonth, setCurrentMonth] = useState(() => {
    const now = new Date()
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  })
  const [showDatePicker, setShowDatePicker] = useState(false)

  // 获取数据
  const fetchData = async (pageNum: number, clear = false, consumeDate?: string) => {
    setLoading(true)
    try {
      const dateToUse = consumeDate !== undefined ? consumeDate : currentMonth
      const res = await consumeApi.getList({ page: pageNum, size: pageSize, consumeDate: dateToUse })

      if (res && Array.isArray(res.records)) {
        const newList: Transaction[] = res.records.map((item: TransactionRecord) => ({
          id: item.id,
          title: `${item.consumeCategory} ${ item.description ? `(${item.description})` : '' }`,
          time: item.createTime ? new Date(item.createTime).toLocaleDateString('zh-CN', {
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
          }) : item.date,
          amount: `${item.consumeType === 'income' ? '+' : '-'}${(item.consumeAmount / 100).toFixed(2)}`, // 分转元
          type: item.consumeType === 'income' ? 'income' : 'expense'
        }))

        if (clear) {
          setAllList(newList)
        } else {
          setAllList(prev => [...prev, ...newList])
        }

        setHasMore(newList.length === pageSize)
      }
    } catch (error) {
      console.error('获取交易明细失败:', error)
      Taro.showToast({ title: '获取数据失败', icon: 'none' })
    } finally {
      setLoading(false)
    }
  }

  // 初始化加载
  useEffect(() => {
    // 检查 token，没有 token 则不请求数据
    const token = Taro.getStorageSync('token')
    if (token) {
      fetchData(1, true)
    }
  }, [])

  // 监听刷新事件
  useEffect(() => {
    const handleRefresh = () => {
      const token = Taro.getStorageSync('token')
      if (!token) return

      setPage(1)
      fetchData(1, true)
    }

    Taro.eventCenter.on('refreshTransactionList', handleRefresh)

    return () => {
      Taro.eventCenter.off('refreshTransactionList', handleRefresh)
    }
  }, [])

  // 监听月份变化事件
  useEffect(() => {
    const handleMonthChange = (month: string) => {
      // 当其他地方触发月份变化时（比如添加记录），如果当前页面的月份与传入的月份不同，需要刷新
      const token = Taro.getStorageSync('token')
      if (!token) return

      if (currentMonth !== month) {
        setCurrentMonth(month)
        setPage(1)
        fetchData(1, true, month)
      }
    }

    Taro.eventCenter.on('monthChanged', handleMonthChange)

    return () => {
      Taro.eventCenter.off('monthChanged', handleMonthChange)
    }
  }, [currentMonth])

  // 上拉加载更多
  const handleScrollToLower = () => {
    const token = Taro.getStorageSync('token')
    if (!token) return

    if (!loading && hasMore) {
      setPage(prev => prev + 1)
      fetchData(page + 1)
    }
  }

  const handleChangeMonth = (month: string) => {
    const token = Taro.getStorageSync('token')
    if (!token) return

    setCurrentMonth(month)
    setPage(1)
    fetchData(1, true, month)
    // 触发月份变化事件
    Taro.eventCenter.trigger('monthChanged', month)
  }

  return (
    <View className='transaction-list'>
      <View className='transaction-list__header'>
        <Text className='transaction-list__title'>交易明细</Text>
        <View className='transaction-list__month-wrapper'>
          <Text
            className='transaction-list__month'
            onClick={() => setShowDatePicker(true)}
          >
            {currentMonth
              ? `${currentMonth.split('-')[0]}年${currentMonth.split('-')[1]}月`
              : `${new Date().getFullYear()}年${String(new Date().getMonth() + 1).padStart(2, '0')}月`}
          </Text>
          {!currentMonth && (
            <Text className='transaction-list__month-tips'>（点击选择月份）</Text>
          )}

          {/* 日期选择器 */}
          <DatePicker
            visible={showDatePicker}
            type='year-month'
            startDate={(() => {
              const now = new Date()
              const oneYearAgo = new Date(now.getFullYear() - 1, now.getMonth(), 1)
              return oneYearAgo
            })()}
            endDate={new Date()}
            value={currentMonth ? new Date(`${currentMonth}-01`) : new Date()}
            onConfirm={(_selectedOptions, selectedValue) => {
              const [year, month] = selectedValue as string[]
              const formattedMonth = `${year}-${String(month).padStart(2, '0')}`
              handleChangeMonth(formattedMonth)
              setShowDatePicker(false)
            }}
            onClose={() => setShowDatePicker(false)}
            onCancel={() => setShowDatePicker(false)}
            title='选择月份'
            formatter={(type, option) => {
              if (type === 'year') return { ...option, label: `${option.value}年` }
              if (type === 'month') return { ...option, label: `${option.value}月` }
              return option
            }}
          />
        </View>
      </View>
      <ScrollView
        className='transaction-list__content'
        scrollY
        onScrollToLower={handleScrollToLower}
      >
        {allList.length === 0 ? (
          <View className='transaction-list__empty'>
            <Text>暂无交易记录</Text>
          </View>
        ) : (
          allList.map((item) => (
            <Cell
              key={item.id}
              className={`transaction-list__item ${item.type}`}
              title={item.title}
              description={item.time}
              extra={
                <Text className={`transaction-list__amount ${item.type === 'income' ? 'income' : 'expense'}`}>
                  {item.amount}
                </Text>
              }
            />
          ))
        )}

        {loading && (
          <View className='transaction-list__loading'>
            <Loading type='spinner' />
            <Text>加载中...</Text>
          </View>
        )}

        {!loading && !hasMore && allList.length > 0 && (
          <View className='transaction-list__nomore'>
            <Text>—— 没有更多了 ——</Text>
          </View>
        )}
      </ScrollView>
    </View>
  )
}
