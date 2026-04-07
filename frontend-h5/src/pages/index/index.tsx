import { View } from '@tarojs/components'
import { Button, Cell, ConfigProvider, Space } from '@nutui/nutui-react-taro'
import '@nutui/nutui-react-taro/dist/style.css'
import './index.scss'

export default function Index() {
  return (
    <ConfigProvider>
      <View className='page'>
        <Cell title='Taro + NutUI' desc='H5 / Weapp baseline ready' />
        <Space direction='vertical' style={{ width: '100%' }}>
          <Button type='primary'>NutUI Button</Button>
          <Button type='success' plain>
            Cross-platform setup OK
          </Button>
        </Space>
      </View>
    </ConfigProvider>
  )
}
