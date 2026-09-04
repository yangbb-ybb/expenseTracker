import { View } from '@tarojs/components'
import { useCounterStore } from '@/store'

export default function Demo() {
  const { count, inc, dec } = useCounterStore()

  return (
    <View style={{ padding: 24 }}>
      <View>Count: {count}</View>
      <View style={{ marginTop: 16, display: 'flex', gap: 8 }}>
        <button onClick={inc}>+</button>
        <button onClick={dec}>-</button>
      </View>
    </View>
  )
}
