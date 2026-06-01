import { View, Text } from '@tarojs/components'
import { Popup, Radio, RadioGroup, Input, Button } from '@nutui/nutui-react-taro'
import Taro from '@tarojs/taro'
import { useState, useEffect } from 'react'
import './AddRecordPopup.scss'
import style from './index.module.scss'

interface AddRecordPopupProps {
  visible: boolean
  onClose: () => void
  onConfirm?: (data: {
    type: 'income' | 'expense'
    category: string
    amount: number
    description?: string
    date?: string
    createTime?: string
  }) => void
}

export default function AddRecordPopup({ visible, onClose, onConfirm }: AddRecordPopupProps) {
  const [type, setType] = useState<'income' | 'expense'>('expense')
  const [category, setCategory] = useState('')
  const [amount, setAmount] = useState('')
  const [description, setDescription] = useState('')

  const expenseCategories = ['餐饮', '购物', '交通', '娱乐', '医疗', '教育', '生活', '其他']
  const incomeCategories = ['工资', '奖金', '理财', '兼职', '其他']

  // 当类型切换时，清空分类选择
  useEffect(() => {
    setCategory('')
  }, [type])

  // 当弹窗关闭时重置表单
  useEffect(() => {
    if (!visible) {
      resetForm()
    }
  }, [visible])

  const resetForm = () => {
    setType('expense')
    setCategory('')
    setAmount('')
    setDescription('')
  }

  const handleSubmit = () => {
    if (!amount || !category) {
      Taro.showToast({ title: '请填写完整信息', icon: 'none' })
      return
    }

    // 验证金额是否为正数
    const amountValue = parseFloat(amount)
    if (isNaN(amountValue) || amountValue <= 0) {
      Taro.showToast({ title: '金额必须大于 0', icon: 'none' })
      return
    }

    // 后端金额单位为分，需要 * 100
    // 同时记录当前时间
    const recordData = {
      type,
      category,
      amount: Math.round(amountValue * 100), // 转换为分
      description: description || undefined,
      date: new Date().toISOString().split('T')[0], // 格式: YYYY-MM-DD
      createTime: new Date().toISOString() // ISO 8601 格式
    }

    onConfirm?.(recordData)
  }

  return (
    <Popup
      visible={visible}
      position='bottom'
      round
      onClose={onClose}
      style={{ height: '70%' }}
      closeable={false}
    >
      <View className='add-record-popup'>
        <View className='add-record-header'>
          <Text className='add-record-title'>添加交易记录</Text>
          <Text className='add-record-close' onClick={onClose}>
            ✕
          </Text>
        </View>

        <View className='add-record-content'>
          <View className='add-record-row'>
            <Text className='add-record-label'>类型</Text>
            <RadioGroup className={ `custom-radio ${style.radioBox}` } value={type} direction="horizontal" onChange={(value: any) => setType(value as 'income' | 'expense')}>
              <Radio value='expense'>支出</Radio>
              <Radio value='income'>收入</Radio>
            </RadioGroup>
          </View>

          <View className='add-record-row'>
            <Text className='add-record-label'>分类</Text>
            <View className='category-list'>
              {(type === 'expense' ? expenseCategories : incomeCategories).map((item) => (
                <View
                  key={item}
                  className={`category-item ${category === item ? 'active' : ''}`}
                  onClick={() => setCategory(item)}
                >
                  <Text>{item}</Text>
                </View>
              ))}
            </View>
          </View>

          <View className='add-record-row'>
            <Text className='add-record-label'>金额</Text>
            <Input
              className='add-record-input'
              type='digit'
              placeholder='请输入金额'
              value={amount}
              onChange={(value) => setAmount(value)}
            />
          </View>

          <View className='add-record-row'>
            <Text className='add-record-label'>备注</Text>
            <Input
              className='add-record-input'
              placeholder='请输入备注（可选）'
              value={description}
              onChange={(value) => setDescription(value)}
            />
          </View>

          <View className='add-record-actions'>
            {/* <Button type='default' onClick={onClose} block>
              取消
            </Button> */}
            <Button type='primary' onClick={handleSubmit} block color='var(--theme-primary)'>
              保存
            </Button>
          </View>
        </View>
      </View>
    </Popup>
  )
}
