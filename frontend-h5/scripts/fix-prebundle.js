const fs = require('fs');
const path = require('path');

const file = path.join(__dirname, '../node_modules/@tarojs/webpack5-prebundle/dist/web.js');

if (fs.existsSync(file)) {
  let content = fs.readFileSync(file, 'utf8');
  if (content.includes('asyncFunction: true')) {
    content = content.replace(/asyncFunction: true,/g, 'templateLiteral: true,');
    fs.writeFileSync(file, content);
    console.log('✅ Fixed @tarojs/webpack5-prebundle webpack config');
  }
}
