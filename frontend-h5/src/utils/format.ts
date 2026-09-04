export function maskPhone(str: string) {
  if (/^\d{11}$/.test(str)) {
    return str.slice(0, 3) + '****' + str.slice(7)
  }
  return str
}
