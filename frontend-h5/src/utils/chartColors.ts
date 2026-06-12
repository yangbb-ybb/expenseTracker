/** 支出分类颜色映射 */
export const CATEGORY_COLORS: Record<string, string> = {
  餐饮: '#FF6B6B',
  购物: '#4ECDC4',
  交通: '#45B7D1',
  娱乐: '#96CEB4',
  医疗: '#FF9F43',
  教育: '#DDA0DD',
  生活: '#54A0FF',
  其他: '#B2BEC3',
}

/**
 * 获取分类对应颜色
 * @param category 分类名称
 * @returns 颜色值，未匹配时返回默认灰色
 */
export const getCategoryColor = (category: string): string => {
  return CATEGORY_COLORS[category] || '#B2BEC3'
}
