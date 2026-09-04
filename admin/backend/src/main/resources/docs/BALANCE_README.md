# 余额管理系统使用文档

## 目录

1. [概述](#概述)
2. [数据库设计](#数据库设计)
3. [快速开始](#快速开始)
4. [API 接口文档](#api-接口文档)
5. [业务场景示例](#业务场景示例)
6. [注意事项](#注意事项)

---

## 概述

本系统提供了完整的用户余额管理功能，包括：
- **用户余额表**：存储用户的余额数据
- **交易流水表**：记录所有的余额变动流水
- **余额操作**：充值、消费、退款、冻结、解冻
- **余额查询**：查询当前余额和可用余额
- **流水查询**：分页查询交易流水，支持筛选
- **统计功能**：系统整体余额统计

---

## 数据库设计

### 1. 用户余额表 (`user_balance`)

存储用户的余额信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID（唯一索引，关联 sys_user 表） |
| balance | DECIMAL(20,2) | 当前总余额 |
| available_balance | DECIMAL(20,2) | 可用余额（未冻结部分） |
| frozen_balance | DECIMAL(20,2) | 冻结余额 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**特性：**
- 每个用户只有一条余额记录
- `balance = available_balance + frozen_balance`
- 支持逻辑删除（`deleted` 字段）

### 2. 交易流水表 (`transaction_record`)

记录所有的余额变动流水。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| type | TINYINT | 交易类型：1=充值, 2=消费, 3=退款, 4=提现, 5=冻结/解冻 |
| transaction_no | VARCHAR(64) | 流水号（唯一，防止重复提交） |
| amount | DECIMAL(20,2) | 变动金额（正数=收入，负数=支出） |
| balance_after | DECIMAL(20,2) | 变动后余额 |
| order_no | VARCHAR(64) | 业务订单号（可选） |
| description | VARCHAR(255) | 交易描述 |
| status | TINYINT | 状态：1=成功, 2=失败, 3=处理中 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**特性：**
- 每次余额变动都会生成一条流水记录
- 流水号全局唯一，可用于幂等性控制
- 支持按用户、订单号、时间查询

---

## 快速开始

### 1. 初始化数据库

执行建表脚本：
```bash
# 进入数据库
mysql -u root -p

# 选择数据库
USE your_database_name;

# 执行建表脚本
SOURCE admin/backend/sql/init_balance_tables.sql;
```

建表脚本位置：`admin/backend/sql/init_balance_tables.sql`

### 2. 后端接口访问

启动后端服务后，可以通过以下方式访问：

- **API 基础路径**：`http://localhost:8080/api`
- **Knife4j 接口文档**：`http://localhost:8080/api/doc.html`

### 3. 前端对接

前端可以通过 HTTP 请求调用余额接口，需要在请求头中携带 Token：
```
Authorization: Bearer <your-token>
```

---

## API 接口文档

### 1. 查询用户余额

**接口地址：** `GET /balance`

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "balance": 100.00,
    "availableBalance": 80.00
  }
}
```

---

### 2. 充值

**接口地址：** `POST /balance/recharge`

**请求体：**
```json
{
  "userId": 1,
  "amount": 50.00,
  "orderNo": "ORDER_20260527001",
  "description": "用户充值"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| amount | BigDecimal | 是 | 充值金额（必须 > 0） |
| orderNo | String | 否 | 业务订单号 |
| description | String | 否 | 描述 |

**响应：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 3. 消费（扣款）

**接口地址：** `POST /balance/consume`

**请求体：**
```json
{
  "userId": 1,
  "amount": 30.00,
  "orderNo": "ORDER_20260527002",
  "description": "购买商品"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| amount | BigDecimal | 是 | 消费金额（必须 > 0） |
| orderNo | String | 否 | 业务订单号 |
| description | String | 否 | 描述 |

**注意：** 消费时会检查可用余额是否充足，不足时返回失败。

---

### 4. 退款

**接口地址：** `POST /balance/refund`

**请求体：**
```json
{
  "userId": 1,
  "amount": 10.00,
  "orderNo": "ORDER_20260527003",
  "description": "订单退款"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| amount | BigDecimal | 是 | 退款金额（必须 > 0） |
| orderNo | String | 否 | 业务订单号 |
| description | String | 否 | 描述 |

---

### 5. 冻结金额

**接口地址：** `POST /balance/freeze`

**请求体：**
```json
{
  "userId": 1,
  "amount": 20.00,
  "orderNo": "ORDER_20260527004",
  "description": "订单预占"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| amount | BigDecimal | 是 | 冻结金额（必须 > 0） |
| orderNo | String | 否 | 业务订单号 |
| description | String | 否 | 描述 |

**注意：** 冻结时会检查可用余额是否充足，冻结后该部分金额不可使用。

---

### 6. 解冻金额

**接口地址：** `POST /balance/unfreeze`

**请求体：**
```json
{
  "userId": 1,
  "amount": 20.00,
  "orderNo": "ORDER_20260527004",
  "description": "订单完成，释放冻结金额"
}
```

**参数说明：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| amount | BigDecimal | 是 | 解冻金额（必须 > 0） |
| orderNo | String | 否 | 业务订单号 |
| description | String | 否 | 描述 |

---

### 7. 查询交易流水

**接口地址：** `GET /balance/records`

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| type | Integer | 否 | 交易类型：1=充值, 2=消费, 3=退款, 4=提现, 5=冻结/解冻 |
| page | Integer | 否 | 页码（默认 1） |
| size | Integer | 否 | 每页数量（默认 10，最大 100） |
| startTime | String | 否 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| endTime | String | 否 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |

**示例 1：查询用户的所有流水（分页）**

```bash
curl -X GET "http://localhost:8080/api/balance/records?userId=1&page=1&size=10"
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1001,
        "userId": 1,
        "type": 1,
        "typeName": "充值",
        "transactionNo": "TXN1716823456789ABC123",
        "amount": 100.00,
        "balanceAfter": 100.00,
        "orderNo": "ORDER_20260527001",
        "description": "用户充值",
        "status": 1,
        "statusName": "成功",
        "createTime": "2026-05-27 15:30:00"
      }
    ],
    "total": 15,
    "size": 10,
    "current": 1,
    "pages": 2
  }
}
```

**示例 2：查询用户的充值记录（最近 7 天）**

```bash
curl -X GET "http://localhost:8080/api/balance/records?userId=1&type=1&startTime=2026-05-20%2000:00:00&endTime=2026-05-27%2023:59:59&page=1&size=20"
```

**示例 3：查询用户的消费记录（最近 30 天）**

```bash
curl -X GET "http://localhost:8080/api/balance/records?userId=1&type=2&startTime=2026-04-27%2000:00:00&endTime=2026-05-27%2023:59:59&page=1&size=20"
```

---

### 8. 查询余额统计

**接口地址：** `GET /balance/statistics`

**说明：** 查询系统整体余额统计信息（需要管理员权限）

**请求参数：** 无

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalUsers": 100,
    "totalBalance": 150000.00,
    "totalAvailableBalance": 140000.00,
    "totalFrozenBalance": 10000.00,
    "todayRechargeAmount": 5000.00,
    "todayConsumeAmount": 3000.00,
    "monthRechargeAmount": 50000.00,
    "monthConsumeAmount": 45000.00
  }
}
```

**字段说明：**

| 字段名 | 说明 |
|--------|------|
| totalUsers | 用户总数 |
| totalBalance | 总余额 |
| totalAvailableBalance | 总可用余额 |
| totalFrozenBalance | 总冻结余额 |
| todayRechargeAmount | 今日充值总额 |
| todayConsumeAmount | 今日消费总额 |
| monthRechargeAmount | 本月充值总额 |
| monthConsumeAmount | 本月消费总额 |

**使用场景：**

```bash
# 查询系统整体统计信息
curl -X GET "http://localhost:8080/api/balance/statistics"
```

---

## 业务场景示例

### 场景 1：用户充值

```bash
# 充值 100 元
curl -X POST http://localhost:8080/api/balance/recharge \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 100.00,
    "orderNo": "RECHARGE_001",
    "description": "用户充值"
  }'
```

**结果：**
- 用户总余额增加 100 元
- 生成一条充值流水记录（type=1）

---

### 场景 2：购物支付

```bash
# 消费 50 元购买商品
curl -X POST http://localhost:8080/api/balance/consume \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 50.00,
    "orderNo": "ORDER_001",
    "description": "购买商品A"
  }'
```

**结果：**
- 用户余额减少 50 元
- 生成一条消费流水记录（type=2）

---

### 场景 3：订单冻结（预占金额）

```bash
# 创建订单时冻结 30 元
curl -X POST http://localhost:8080/api/balance/freeze \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 30.00,
    "orderNo": "ORDER_002",
    "description": "订单预占"
  }'
```

**结果：**
- 可用余额减少 30 元
- 冻结余额增加 30 元
- 生成一条冻结流水记录（type=5，amount为负数）

---

### 场景 4：订单完成（释放冻结）

```bash
# 订单完成，从冻结中扣除 30 元
curl -X POST http://localhost:8080/api/balance/unfreeze \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 30.00,
    "orderNo": "ORDER_002",
    "description": "订单完成"
  }'
```

**结果：**
- 冻结余额减少 30 元
- 可用余额增加 30 元
- 生成一条解冻流水记录（type=5，amount为正数）

---

### 场景 5：订单取消（退款）

```bash
# 订单取消，退还未使用的金额
curl -X POST http://localhost:8080/api/balance/refund \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 20.00,
    "orderNo": "ORDER_003",
    "description": "订单取消"
  }'
```

**结果：**
- 用户余额增加 20 元
- 生成一条退款流水记录（type=3）

---

## 注意事项

### 1. 事务保证

所有余额操作都使用了 `@Transactional` 注解，保证：
- 余额更新和流水记录在同一事务中
- 操作失败时自动回滚
- 不会出现余额和流水不一致的情况

### 2. 幂等性

- 每条流水都有唯一的 `transaction_no`（流水号）
- 建议在调用接口时传入业务订单号 `orderNo`
- 防止重复提交导致重复扣款或充值

### 3. 余额检查

- **消费**：检查 `available_balance >= amount`
- **冻结**：检查 `available_balance >= amount`
- **解冻**：检查 `frozen_balance >= amount`
- 余额不足时，操作会失败并返回错误信息

### 4. 数据一致性

- 系统保证：`balance = available_balance + frozen_balance`
- 每次操作都会同时更新这两个字段
- 不要直接修改数据库字段，应该通过接口操作

### 5. 流水查询

可以通过以下方式查询流水：

```java
// 按用户查询
LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(TransactionRecord::getUserId, userId)
       .orderByDesc(TransactionRecord::getCreateTime);
List<TransactionRecord> records = transactionRecordRepository.selectList(wrapper);
```

### 6. 错误处理

所有接口返回统一格式：
- `code=200`：操作成功
- `code!=200`：操作失败，`message` 字段包含错误信息

---

## 相关文件

- **实体类：**
  - [UserBalance.java](../java/com/example/entity/po/UserBalance.java)
  - [TransactionRecord.java](../java/com/example/entity/po/TransactionRecord.java)

- **数据访问层：**
  - [UserBalanceRepository.java](../java/com/example/repository/UserBalanceRepository.java)
  - [TransactionRecordRepository.java](../java/com/example/repository/TransactionRecordRepository.java)

- **业务层：**
  - [BalanceService.java](../java/com/example/service/BalanceService.java)
  - [BalanceServiceImpl.java](../java/com/example/service/impl/BalanceServiceImpl.java)

- **控制器：**
  - [BalanceController.java](../java/com/example/controller/BalanceController.java)

- **VO/DTO：**
  - [BalanceVO.java](../java/com/example/entity/vo/BalanceVO.java)
  - [BalanceStatisticsVO.java](../java/com/example/entity/vo/BalanceStatisticsVO.java)
  - [TransactionRecordVO.java](../java/com/example/entity/vo/TransactionRecordVO.java)
  - [BalanceRechargeDTO.java](../java/com/example/entity/dto/BalanceRechargeDTO.java)
  - [BalanceConsumeDTO.java](../java/com/example/entity/dto/BalanceConsumeDTO.java)
  - [BalanceRefundDTO.java](../java/com/example/entity/dto/BalanceRefundDTO.java)
  - [BalanceFreezeDTO.java](../java/com/example/entity/dto/BalanceFreezeDTO.java)
  - [BalanceUnfreezeDTO.java](../java/com/example/entity/dto/BalanceUnfreezeDTO.java)
  - [TransactionRecordQueryDTO.java](../java/com/example/entity/dto/TransactionRecordQueryDTO.java)

- **SQL 脚本：**
  - [init_balance_tables.sql](../../sql/init_balance_tables.sql)

---

## 常见问题

### Q1: 如何处理并发扣款？

A: 系统使用数据库事务保证一致性。如果需要更强的并发控制，可以考虑：
- 使用数据库乐观锁（version 字段）
- 使用 Redis 分布式锁
- 数据库行级锁（SELECT ... FOR UPDATE）

### Q2: 流水记录太多怎么办？

A: 建议：
- 定期归档历史流水（如 1 年前的数据）
- 按月份分表
- 使用读写分离，查询走从库

### Q3: 如何支持提现功能？

A: 可以在 `BalanceService` 中添加 `withdraw` 方法，流程：
1. 检查余额充足
2. 扣除余额
3. 生成提现流水（type=4）
4. 调用第三方支付接口

### Q4: 如何查询用户的消费记录？

A: 
```java
LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(TransactionRecord::getUserId, userId)
       .eq(TransactionRecord::getType, 2)  // 2=消费
       .orderByDesc(TransactionRecord::getCreateTime);
List<TransactionRecord> records = transactionRecordRepository.selectList(wrapper);
```

### Q5: 如何分页查询流水？

A: 使用 `GET /balance/records` 接口，传入 `page` 和 `size` 参数：

```bash
# 查询第 2 页，每页 20 条
curl -X GET "http://localhost:8080/api/balance/records?userId=1&page=2&size=20"
```

### Q6: 如何按时间范围查询流水？

A: 使用 `startTime` 和 `endTime` 参数：

```bash
# 查询 2026-05-27 当天的流水
curl -X GET "http://localhost:8080/api/balance/records?userId=1&startTime=2026-05-27%2000:00:00&endTime=2026-05-27%2023:59:59"
```

### Q7: 前端如何展示流水记录？

A: 前端可以使用 `TransactionRecordVO` 的字段：
- `typeName`：交易类型名称（如"充值"、"消费"）
- `amount`：变动金额（正数为收入，负数为支出）
- `balanceAfter`：变动后余额
- `description`：交易描述
- `createTime`：交易时间

建议按时间倒序展示，最新的流水在最前面。

### Q8: 如何查询系统整体统计信息？

A: 使用 `GET /balance/statistics` 接口：

```bash
# 查询统计信息
curl -X GET "http://localhost:8080/api/balance/statistics"
```

返回数据包含：
- 总用户数、总余额、总可用余额、总冻结余额
- 今日充值总额、今日消费总额
- 本月充值总额、本月消费总额

---

## 版本记录

- **v1.2.0** (2026-05-27)
  - 新增：余额统计接口（总余额、今日消费、本月充值等）
  - 新增：BalanceStatisticsVO

- **v1.1.0** (2026-05-27)
  - 新增：流水查询接口（分页、筛选、时间范围）
  - 新增：流水查询使用文档和示例
  - 新增：TransactionRecordVO、TransactionRecordQueryDTO

- **v1.0.0** (2026-05-27)
  - 初始版本
  - 支持充值、消费、退款、冻结、解冻
  - 完整的流水记录
  - 基础余额查询接口
