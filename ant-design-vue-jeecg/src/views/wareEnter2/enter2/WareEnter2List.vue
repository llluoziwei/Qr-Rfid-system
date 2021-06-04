<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="器材编号">
              <a-input placeholder="请输入器材编号" v-model="queryParam.wareId"></a-input>
            </a-form-item>
          </a-col>
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="目录类型">
              <a-input placeholder="请输入目录类型" v-model="queryParam.kitName"></a-input>
            </a-form-item>
          </a-col> -->
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="入库时间">
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择开始时间"
                class="query-group-cust"
                v-model="queryParam.enterTime_begin"
              ></j-date>
              <span class="query-group-split-cust"></span>
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择结束时间"
                class="query-group-cust"
                v-model="queryParam.enterTime_end"
              ></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <!-- <a-col :xl="10" :lg="11" :md="12" :sm="24">
              <a-form-item label="入库时间">
                <j-date
                  :show-time="true"
                  date-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择开始时间"
                  class="query-group-cust"
                  v-model="queryParam.enterTime_begin"
                ></j-date>
                <span class="query-group-split-cust"></span>
                <j-date
                  :show-time="true"
                  date-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择结束时间"
                  class="query-group-cust"
                  v-model="queryParam.enterTime_end"
                ></j-date>
              </a-form-item>
            </a-col> -->
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('入库申请2')">导出</a-button>
      <!-- <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">取消选择</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="wareId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
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

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <div class="affirm">
      <span>
        <a-button type="primary" :loading="loading" @click="enterLoading"> 提交申请 </a-button>
      </span>
    </div>

    <ware-enter2-modal ref="modalForm" @ok="modalFormOk"></ware-enter2-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin2 } from '@/mixins/JeecgListMixin2'
import WareEnter2Modal from './modules/WareEnter2Modal'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'WareEnter2List',
  mixins: [JeecgListMixin2, mixinDevice],
  components: {
    WareEnter2Modal,
  },
  data() {
    return {
      // 提交申请按钮
      loading: false,

      description: '入库申请2管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: 'id',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '器材编号',
          align: 'center',
          sorter: true,
          dataIndex: 'wareId',
        },
        {
          title: '器材数量',
          align: 'center',
          dataIndex: 'wareNum',
        },
        {
          title: '目录类型',
          align: 'center',
          dataIndex: 'kitName',
        },
        {
          title: '二维码绑定',
          align: 'center',
          dataIndex: 'qrBind',
        },
        {
          title: 'RFID绑定',
          align: 'center',
          dataIndex: 'rfidBind',
        },
        {
          title: '入库时间',
          align: 'center',
          sorter: true,
          dataIndex: 'enterTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        // list:'/wareenter2/wareEnter2/queryByCreateTime',
        delete: '/wareenter2/wareEnter2/delete',
        deleteBatch: '/wareenter2/wareEnter2/deleteBatch',
        exportXlsUrl: '/wareenter2/wareEnter2/exportXls',
        importExcelUrl: 'wareenter2/wareEnter2/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      dataSource: [],
      dataSources: [],
      dataSourcess: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.queryData()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    // btnclick
    enterLoading() {
      this.loading = { delay: 500 }
      setTimeout(() => {
        this.loading = false
        this.dataSource = []
      }, 2000)
    },
    queryData() {
      let url = '/wareenter2/wareEnter2/queryByCreateTime'
      getAction(url).then((res) => {
        if (res.success) {
          // console.log(url);
          console.log(res)
          this.$refs.modalForm.add()
          let arr = []
          arr.push(res.result)
          // console.log(res.result);
          // this.dataSources = arr
          // console.log(this.dataSource);
          // this.$refs.modalForm.add();
          // if(this.$refs.modalForm.disableSubmit = true){
          //   this.dataSource1 = res.result.records||res.result;
          //   console.log(123);
          // }
        }
      })
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'wareId', text: '器材编号', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'wareNum', text: '器材数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kitName', text: '目录类型' })
      fieldList.push({ type: 'string', value: 'qrBind', text: '二维码绑定', dictCode: '' })
      fieldList.push({ type: 'string', value: 'rfidBind', text: 'RFID绑定', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'enterTime', text: '入库时间' })
      this.superFieldList = fieldList
    },
    modalFormOk(data) {
      // var arr=[];
      // arr.push(data);
      console.log(data)
      this.dataSourcess.push(data)
      // console.log(this.dataSourcess);
      this.dataSource = this.dataSourcess
      console.log(this.dataSource)
    },
  },
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
// /deep/ .ant-table-row-level-0:nth-child(-n + 4) {
//   background-color: rgba(26, 196, 158, 0.2);
// }
.affirm {
  width: 100%;
  margin-top: 20px;
  text-align: center;
}
</style>