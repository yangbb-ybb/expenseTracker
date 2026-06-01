import { View, Text, ScrollView } from '@tarojs/components'
import { Cell, Loading } from '@nutui/nutui-react-taro'
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
  type: string
  category: string
  amount: number
  description: string
  createTime: string
}

export default function TransactionList({ list = [] }: TransactionListProps) {
  // 分页状态
  const [page, setPage] = useState(1)
  const pageSize = 10
  const [loading, setLoading] = useState(false)
  const [allList, setAllList] = useState<Transaction[]>([])
  const [hasMore, setHasMore] = useState(true)

  // 获取数据
  const fetchData = async (pageNum: number, clear = false) => {
    setLoading(true)
    try {
      const res = await consumeApi.getList({ page: pageNum, size: pageSize })

      if (res && Array.isArray(res.records)) {
        const newList: Transaction[] = res.records.map((item: TransactionRecord) => ({
          id: item.id,
          title: item.description || `${item.category}`,
          time: item.createTime ? new Date(item.createTime).toLocaleDateString('zh-CN', {
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
          }) : item.date,
          amount: `${item.type === 'income' ? '+' : '-'}${Number(item.amount).toFixed(2)}`,
          type: item.type === 'income' ? 'income' : 'expense'
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
    fetchData(1, true)
  }, [])

  // 上拉加载更多
  const handleScrollToLower = () => {
    if (!loading && hasMore) {
      setPage(prev => prev + 1)
      fetchData(page + 1)
    }
  }

  return (
    <View className='transaction-list'>
      <View className='transaction-list__header'>
        <Text className='transaction-list__title'>交易明细</Text>
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
