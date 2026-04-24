import { View } from '@tarojs/components'
import { Button, Cell, ConfigProvider, Space } from '@nutui/nutui-react-taro'
import './index.scss'

export default function Index() {
  return (
    <ConfigProvider>
      <View className='page'>
        <Cell title='Taro + NutUI 1233' desc='H5 / Weapp baseline ready' />
        <Space direction='vertical' style={{ width: '100%' }}>
          <Button type='primary'>NutUI Button 驯兽</Button>
          <Button type='success' plain>
            Cross-platform setup
          </Button>
        </Space>
      </View>
    </ConfigProvider>
  )
}
