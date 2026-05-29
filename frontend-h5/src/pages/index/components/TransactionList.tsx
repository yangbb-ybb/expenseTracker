import { View, Text } from '@tarojs/components'
import { Cell } from '@nutui/nutui-react-taro'
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

const defaultList: Transaction[] = [
  { id: 1, title: '购物返利', time: '05-26 14:30', amount: '+12.50', type: 'income' },
  { id: 2, title: '分享奖励', time: '05-26 10:15', amount: '+8.00', type: 'income' },
  { id: 3, title: '兑换商品', time: '05-25 16:45', amount: '-66.00', type: 'expense' },
  { id: 4, title: '新手任务奖励', time: '05-25 09:00', amount: '+28.88', type: 'income' },
]

export default function TransactionList({ list = defaultList }: TransactionListProps) {
  return (
    <View className='transaction-list'>
      <View className='transaction-list__header'>
        <Text className='transaction-list__title'>交易明细</Text>
        <Text className='transaction-list__more'>查看全部</Text>
      </View>
      <View className='transaction-list__content'>
        {list.map((item) => (
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
        ))}
      </View>
    </View>
  )
}
