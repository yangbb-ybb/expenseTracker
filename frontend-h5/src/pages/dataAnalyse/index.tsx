import { View, Text } from '@tarojs/components'
import { DatePicker, Loading } from '@nutui/nutui-react-taro'
import Taro from '@tarojs/taro'
import { useEffect, useRef, useState } from 'react'
import * as echarts from 'echarts/core'
import { PieChart } from 'echarts/charts'
import { TooltipComponent, LegendComponent } from 'echarts/components'
import { LabelLayout } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'
import { consumeApi } from '@/api/consume'
import { getCategoryColor } from '@/utils/chartColors'
import './index.scss'

// 注册 ECharts 组件
echarts.use([PieChart, TooltipComponent, LegendComponent, LabelLayout, CanvasRenderer])

interface ConsumeRecord {
  id: number
  consumeType: string
  consumeCategory: string
  consumeAmount: number
}

interface CategoryData {
  name: string
  value: number
  color: string
}

export default function DataAnalyse() {
  const [currentMonth, setCurrentMonth] = useState(() => {
    const now = new Date()
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  })
  const [showDatePicker, setShowDatePicker] = useState(false)
  const [loading, setLoading] = useState(false)
  const [currentType, setCurrentType] = useState<'expense' | 'income'>('expense')
  const [categoryData, setCategoryData] = useState<CategoryData[]>([])
  const [totalAmount, setTotalAmount] = useState(0)

  const chartInstanceRef = useRef<echarts.EChartsType | null>(null)
  const categoryDataRef = useRef<CategoryData[]>([])

  // 保持 ref 与 state 同步，避免闭包问题
  useEffect(() => {
    categoryDataRef.current = categoryData
  }, [categoryData])

  // 初始化图表实例
  useEffect(() => {
    const initChart = () => {
      const dom = document.getElementById('dataAnalyseChart') as HTMLDivElement | null
      console.log('chart dom:', dom)
      if (dom) {
        chartInstanceRef.current = echarts.init(dom)
        console.log('echarts init success')
        // 初始化完成后，用 ref 中的最新数据渲染
        renderChart(categoryDataRef.current)
      }
    }

    // Taro H5 渲染可能有延迟，延迟获取 DOM
    const timer = setTimeout(initChart, 100)

    const handleResize = () => {
      chartInstanceRef.current?.resize()
    }
    window.addEventListener('resize', handleResize)

    return () => {
      clearTimeout(timer)
      window.removeEventListener('resize', handleResize)
      chartInstanceRef.current?.dispose()
      chartInstanceRef.current = null
    }
  }, [])

  // 数据变化时渲染图表
  useEffect(() => {
    renderChart(categoryData)
  }, [categoryData])

  // 获取数据并渲染图表
  const fetchData = async (month: string) => {
    const token = Taro.getStorageSync('token')
    console.log('fetchData month:', month, 'token:', token ? 'exists' : 'missing')
    if (!token) return

    setLoading(true)
    try {
      const res: any = await consumeApi.getList({
        page: 1,
        size: 1000,
        consumeDate: month,
      })
      console.log('getList res:', res)

      const records: ConsumeRecord[] = res?.records || []
      console.log('records count:', records.length)

      // 只统计支出，并按分类汇总
      const categoryMap = new Map<string, number>()
      records
        .filter((item) => item.consumeType === currentType)
        .forEach((item) => {
          const category = item.consumeCategory || '其他'
          const amount = item.consumeAmount || 0
          categoryMap.set(category, (categoryMap.get(category) || 0) + amount)
        })

      const data: CategoryData[] = Array.from(categoryMap.entries())
        .map(([name, value]) => ({
          name,
          value: value / 100, // 分转元
          color: getCategoryColor(name, currentType),
        }))
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((sum, item) => sum + item.value, 0)
      console.log('categoryData:', data, 'total:', total)

      setCategoryData(data)
      setTotalAmount(total)
    } catch (error) {
      console.error('获取消费数据失败:', error)
      Taro.showToast({ title: '获取数据失败', icon: 'none' })
    } finally {
      setLoading(false)
    }
  }

  // 渲染或更新图表
  const renderChart = (data: CategoryData[]) => {
    console.log('renderChart called, instance:', chartInstanceRef.current, 'data:', data)
    if (!chartInstanceRef.current) return

    if (data.length === 0) {
      chartInstanceRef.current.clear()
      return
    }

    const option: echarts.EChartsCoreOption = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => {
          return `${params.name}<br/>${params.value.toFixed(2)}元 (${params.percent}%)`
        },
      },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: true,
          itemStyle: {
            borderRadius: 6,
            borderColor: '#fff',
            borderWidth: 2,
          },
          label: {
            show: true,
            formatter: '{b}\n{d}%',
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold',
            },
          },
          data: data.map((item) => ({
            value: item.value,
            name: item.name,
            itemStyle: { color: item.color },
          })),
        },
      ],
    }

    chartInstanceRef.current.setOption(option, true)
  }

  // 月份或类型变化时重新加载
  useEffect(() => {
    fetchData(currentMonth)
  }, [currentMonth, currentType])

  const handleChangeMonth = (month: string) => {
    setCurrentMonth(month)
    setShowDatePicker(false)
  }

  return (
    <View className='data-analyse-page'>
      {/* 月份选择器 */}
      <View className='month-selector' onClick={() => setShowDatePicker(true)}>
        <Text className='month-selector__label'>{currentMonth.split('-')[0]}年{currentMonth.split('-')[1]}月</Text>
        <Text className='month-selector__arrow'>▼</Text>
      </View>

      {/* 收支类型切换 */}
      <View className='type-switcher'>
        <View
          className={`type-switcher__item ${currentType === 'expense' ? 'active' : ''}`}
          onClick={() => setCurrentType('expense')}
        >
          <Text>支出</Text>
        </View>
        <View
          className={`type-switcher__item ${currentType === 'income' ? 'active' : ''}`}
          onClick={() => setCurrentType('income')}
        >
          <Text>收入</Text>
        </View>
      </View>

      <DatePicker
        visible={showDatePicker}
        type='year-month'
        startDate={(() => {
          const now = new Date()
          return new Date(now.getFullYear() - 1, now.getMonth(), 1)
        })()}
        endDate={new Date()}
        value={new Date(`${currentMonth}-01`)}
        onConfirm={(_selectedOptions, selectedValue) => {
          const [year, month] = selectedValue as string[]
          handleChangeMonth(`${year}-${String(month).padStart(2, '0')}`)
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

      {/* 图表卡片 */}
      <View className='chart-card'>
        <View className='chart-card__header'>
          <Text className='chart-card__title'>
            {currentType === 'expense' ? '支出构成' : '收入构成'}
          </Text>
          <Text className='chart-card__total'>
            {currentType === 'expense' ? '总支出' : '总收入'}：¥{totalAmount.toFixed(2)}
          </Text>
        </View>

        <View className='chart-wrapper'>
          <View id='dataAnalyseChart' className='chart-container' />

          {loading && (
            <View className='chart-card__loading chart-overlay'>
              <Loading type='spinner' />
              <Text>加载中...</Text>
            </View>
          )}

          {!loading && categoryData.length === 0 && (
            <View className='chart-card__empty chart-overlay'>
              <Text>暂无{currentType === 'expense' ? '支出' : '收入'}数据</Text>
            </View>
          )}
        </View>

        {!loading && categoryData.length > 0 && (
          <View className='legend-list'>
            {categoryData.map((item) => {
              const percent = totalAmount > 0 ? ((item.value / totalAmount) * 100).toFixed(1) : '0.0'
              return (
                <View key={item.name} className='legend-item'>
                  <View className='legend-item__dot' style={{ background: item.color }} />
                  <Text className='legend-item__name'>{item.name}</Text>
                  <Text className='legend-item__amount'>¥{item.value.toFixed(2)}</Text>
                  <Text className='legend-item__percent'>{percent}%</Text>
                </View>
              )
            })}
          </View>
        )}
      </View>
    </View>
  )
}
