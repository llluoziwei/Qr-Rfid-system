<template>
  <div>
    <a-form style="margin: 40px auto 0">
      <a-alert :closable="true" message="确认盘点后，数据将直接存入库存管理。" style="margin-bottom: 24px" />
      <!-- table区域-begin -->
      <div>
        <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div> -->

        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          class="j-table-force-nowrap"
          :scroll="{ x: true }"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :expandedRowKeys="expandedRowKeys"
          @change="handleTableChange"
          @expand="handleExpand"
          v-bind="tableProps"
        >
          <template slot="imgSlot" slot-scope="text">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
            <img
              v-else
              :src="getImgView(text)"
              height="25px"
              alt=""
              style="max-width: 80px; font-size: 12px; font-style: italic"
            />
          </template>
          <template slot="fileSlot" slot-scope="text">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
            <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
              下载
            </a-button>
          </template>

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <!-- 表格内联分割线| -->
            <!-- <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handleAddChild(record)">添加下级</a>
                </a-menu-item>
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteNode(record.id)" placement="topLeft">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown> -->
          </span>
        </a-table>
      </div>
      <planCheck-modal ref="modalForm" @ok="modalFormOk"></planCheck-modal>
      <!-- table区域-end -->
      <!-- <a-form-item :wrapperCol="{ span: 19, offset: 5 }"> -->
      <div style="text-align: center">
        <a-button :loading="loading" type="primary" @click="nextStep"><span>确认盘点单</span></a-button>
        <a-button style="margin-left: 8px" @click="prevStep"><span>上一步</span></a-button>
      </div>
      <!-- </a-form-item> -->
    </a-form>
  </div>
</template>

<script>
import { getAction, deleteAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import PlanCheckModal from './modules/PlanCheckModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import { filterObj } from '@/utils/util'

export default {
  name: 'Plan2',
  // 表格数据
  mixins: [JeecgListMixin],
  components: {
    PlanCheckModal,
  },
  data() {
    return {
      description: '计划盘点管理页面',
      loading: false,
      // 表头
      columns: [
        {
          title: '库房绑定',
          align: 'left',
          // dataIndex: 'depotBind'
          dataIndex: 'depotBind_dictText',
        },
        {
          title: '货架绑定',
          align: 'left',
          sorter: true,
          dataIndex: 'goodsBind_dictText',
        },
        {
          title: '目录类型',
          align: 'left',
          dataIndex: 'kitName_dictText',
        },
        {
          title: '器材编号',
          align: 'left',
          dataIndex: 'wareId',
        },
        {
          title: '包装类型',
          align: 'left',
          dataIndex: 'packType_dictText',
        },
        {
          title: '器材数量',
          align: 'left',
          dataIndex: 'wareNum',
        },
        {
          title: '盘点数量',
          align: 'left',
          dataIndex: 'checkNum',
        },
        {
          title: '盘盈盘亏',
          align: 'left',
          dataIndex: 'profitLess',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/plancheck/planCheck/rootList',
        childList: '/plancheck/planCheck/childList',
        getChildListBatch: '/plancheck/planCheck/getChildListBatch',
        delete: '/plancheck/planCheck/delete',
        deleteBatch: '/plancheck/planCheck/deleteBatch',
        exportXlsUrl: '/plancheck/planCheck/exportXls',
        importExcelUrl: 'plancheck/planCheck/importExcel',
      },
      expandedRowKeys: [],
      hasChildrenField: 'hasChild',
      pidField: 'pid',
      dictOptions: {},
      loadParent: false,
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.getData()
  },
  computed: {
    importExcelUrl() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
    tableProps() {
      let _this = this
      return {
        // 列表项是否可选择
        rowSelection: {
          selectedRowKeys: _this.selectedRowKeys,
          onChange: (selectedRowKeys) => (_this.selectedRowKeys = selectedRowKeys),
        },
      }
    },
  },
  methods: {
    getData() {
      let url = '/wareenter2/wareEnter2/queryByCreateTime'
      getAction(url).then((res) => {
        console.log(res)
      })
    },
    nextStep() {
      let that = this
      that.loading = true
      setTimeout(function () {
        that.$emit('nextStep')
      }, 1500)
    },
    prevStep() {
      this.$emit('prevStep')
    },
    // 表格方法
    loadData(arg) {
      if (arg == 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      let params = this.getQueryParams()
      params.hasQuery = 'true'
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            let result = res.result
            if (Number(result.total) > 0) {
              this.ipagination.total = Number(result.total)
              this.dataSource = this.getDataByResult(res.result.records)
              return this.loadDataByExpandedRows(this.dataSource)
            } else {
              this.ipagination.total = 0
              this.dataSource = []
            }
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    // 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
    loadDataByExpandedRows(dataList) {
      if (this.expandedRowKeys.length > 0) {
        return getAction(this.url.getChildListBatch, { parentIds: this.expandedRowKeys.join(',') }).then((res) => {
          if (res.success && res.result.records.length > 0) {
            //已展开的数据批量子节点
            let records = res.result.records
            const listMap = new Map()
            for (let item of records) {
              let pid = item[this.pidField]
              if (this.expandedRowKeys.join(',').includes(pid)) {
                let mapList = listMap.get(pid)
                if (mapList == null) {
                  mapList = []
                }
                mapList.push(item)
                listMap.set(pid, mapList)
              }
            }
            let childrenMap = listMap
            let fn = (list) => {
              if (list) {
                list.forEach((data) => {
                  if (this.expandedRowKeys.includes(data.id)) {
                    data.children = this.getDataByResult(childrenMap.get(data.id))
                    fn(data.children)
                  }
                })
              }
            }
            fn(dataList)
          }
        })
      } else {
        return Promise.resolve()
      }
    },
    getQueryParams(arg) {
      //获取查询条件
      let sqp = {}
      let param = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      if (arg) {
        param = Object.assign(sqp, this.isorter, this.filters)
      } else {
        param = Object.assign(sqp, this.queryParam, this.isorter, this.filters)
      }
      if (JSON.stringify(this.queryParam) === '{}' || arg) {
        param.hasQuery = 'false'
      } else {
        param.hasQuery = 'true'
      }
      param.field = this.getQueryField()
      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      return filterObj(param)
    },
    searchReset() {
      //重置
      this.expandedRowKeys = []
      this.queryParam = {}
      this.loadData(1)
    },
    getDataByResult(result) {
      if (result) {
        return result.map((item) => {
          //判断是否标记了带有子节点
          if (item[this.hasChildrenField] == '1') {
            let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true }
            item.children = [loadChild]
          }
          return item
        })
      }
    },
    handleExpand(expanded, record) {
      // 判断是否是展开状态
      if (expanded) {
        this.expandedRowKeys.push(record.id)
        if (record.children.length > 0 && record.children[0].isLoading === true) {
          let params = this.getQueryParams(1) //查询条件
          params[this.pidField] = record.id
          params.hasQuery = 'false'
          params.superQueryParams = ''
          getAction(this.url.childList, params).then((res) => {
            if (res.success) {
              if (res.result.records) {
                record.children = this.getDataByResult(res.result.records)
                this.dataSource = [...this.dataSource]
              } else {
                record.children = ''
                record.hasChildrenField = '0'
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      } else {
        let keyIndex = this.expandedRowKeys.indexOf(record.id)
        if (keyIndex >= 0) {
          this.expandedRowKeys.splice(keyIndex, 1)
        }
      }
    },
    handleAddChild(record) {
      this.loadParent = true
      let obj = {}
      obj[this.pidField] = record['id']
      this.$refs.modalForm.add(obj)
    },
    handleDeleteNode(id) {
      if (!this.url.delete) {
        this.$message.error('请设置url.delete属性!')
        return
      }
      var that = this
      deleteAction(that.url.delete, { id: id }).then((res) => {
        if (res.success) {
          that.loadData(1)
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    batchDel() {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return false
      } else {
        let ids = ''
        let that = this
        that.selectedRowKeys.forEach(function (val) {
          ids += val + ','
        })
        that.$confirm({
          title: '确认删除',
          content: '是否删除选中数据?',
          onOk: function () {
            that.handleDeleteNode(ids)
            that.onClearSelected()
          },
        })
      }
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'depotBind', text: '库房绑定', dictCode: 'ware_depot,depot_name,id' })
      fieldList.push({ type: 'string', value: 'goodsBind', text: '货架绑定', dictCode: 'ware_goods,goods_id,id' })
      fieldList.push({ type: 'string', value: 'kitName', text: '目录类型' })
      fieldList.push({ type: 'string', value: 'wareId', text: '器材编号', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'packType', text: '包装类型', dictCode: 'ware_type' })
      fieldList.push({ type: 'BigDecimal', value: 'wareNum', text: '器材数量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'checkNum', text: '盘点数量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'profitLess', text: '盘盈盘亏', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pid', text: '父级节点', dictCode: '' })
      this.superFieldList = fieldList
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@assets/less/common.less';
.stepFormText {
  margin-bottom: 24px;

  .ant-form-item-label,
  .ant-form-item-control {
    line-height: 22px;
  }
}
/deep/ .ant-table-row-level-0:nth-child(-n + 2) {
  background-color: rgba(26, 196, 158, 0.2);
}
/deep/ .ant-table-row-level-1:nth-child(-n + 3) {
  background-color: rgba(26, 196, 158, 0.2);
}
</style>