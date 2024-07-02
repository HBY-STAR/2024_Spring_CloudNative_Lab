module.exports = {
  printWidth: 160, //单行长度   超出长度会换行
  singleAttributePerLine: false, //一个属性一行    配合printWidth更便于阅读
  tabWidth: 2, //缩进长度
  useTabs: false, //使用空格代替tab缩进
  semi: false, //句末使用分号
  singleQuote: true, //使用单引号
  quoteProps: 'as-needed', //仅在必需时为对象的key添加引号
  trailingComma: 'none', //多行时尽可能打印尾随逗号
  bracketSpacing: true, //在对象前后添加空格-eg: { foo: bar }
  bracketSameLine: true,
  arrowParens: 'always', //单参数箭头函数参数周围使用圆括号-eg: (x) => x
  proseWrap: 'never', //不知道怎么翻译
  htmlWhitespaceSensitivity: 'ignore', //对HTML全局空白不敏感
  endOfLine: 'lf', //结束行形式
  vueIndentScriptAndStyle: true, //缩进script和style标签
  embeddedLanguageFormatting: 'auto', //对引用代码进行格式化
  jsxBracketSameLine: true //指定开始标签和结束标签应该在同一行
}
