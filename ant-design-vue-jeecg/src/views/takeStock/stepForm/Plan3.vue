<template>
  <div>
    <a-form style="margin: 40px auto 0">
      <result title="盘点完成" :is-success="true" description="" style="position: relative">
        <div class="information">
          <a-row>
            <a-col :sm="8" :xs="24">盘点仓库：</a-col>
            <a-col :sm="16" :xs="24">{{ depot[0] }}、{{ depot[1] }}</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">盘点时间：</a-col>
            <a-col :sm="16" :xs="24">{{ datatime[0] }}</a-col>
          </a-row>
          <a-divider />
          <!-- 散件盘点 -->
          <a-row>
            <a-col :sm="8" :xs="24">整件器材盘点数：</a-col>
            <a-col :sm="16" :xs="24">6</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">整件器材盘盈/亏数：</a-col>
            <a-col :sm="16" :xs="24">0</a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">整件盘亏率：</a-col>
            <a-col :sm="16" :xs="24"><span class="money">0</span>%</a-col>
          </a-row>
          <a-divider />
          <!-- 整件盘点 -->
          <a-row>
            <a-col :sm="8" :xs="24">散件器材盘点数：</a-col>
            <a-col :sm="16" :xs="24"><span>48</span></a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">散件器材盘盈/亏数：</a-col>
            <a-col :sm="16" :xs="24"><span>-3</span></a-col>
          </a-row>
          <a-row>
            <a-col :sm="8" :xs="24">散件盘亏率：</a-col>
            <a-col :sm="16" :xs="24"><span class="money">6.25</span>%</a-col>
          </a-row>
        </div>
        <div slot="action">
          <a-button type="primary" @click="finish">再次盘点</a-button>
          <a-button style="margin-left: 8px" @click="toOrderList">查看库存</a-button>
        </div>
        <div style="position: absolute; right: 10px; bottom: 80px">
          <a-button type="primary" @click="detailBtn">查看详情</a-button>
        </div>
      </result>
    </a-form>
    <!-- 抽屉 -->
    <a-drawer
      title="盘点详情"
      placement="bottom"
      :closable="false"
      :visible="visible"
      :after-visible-change="afterVisibleChange"
      @close="onClose"
      height="550px"
    >
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="包装类型">
                <j-dict-select-tag placeholder="请选择包装类型" v-model="queryParam.packType" dictCode="ware_type" />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                </a> -->
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
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
          :scroll="{y: 280}"
          rowKey="id"
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

          <!-- <span slot="action" slot-scope="text, record">
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
          </span> -->
        </a-table>
      </div>

      <pl-form-modal ref="modalForm" @ok="modalFormOk"></pl-form-modal>
    </a-drawer>
  </div>
</template>

<script>
import Result from '../../result/Result'
// 表单import
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
// import PlFormModal from './modules2/PlFormModal'
import PlFormModal from './modules2/PlFormModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'

export default {
  name: 'Plan3',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    Result,
    PlFormModal,
  },
  data() {
    return {
      depot: ['库房1', '库房2'],
      datatime: ['2021-04-15 20:35:17'],
      checktime: '2021-04-15 20:45:22',
      loading: false,
      visible: false,
      pStyle: {
        fontSize: '16px',
        color: 'rgba(0,0,0,0.85)',
        lineHeight: '24px',
        display: 'block',
        marginBottom: '16px',
      },
      // 表格data
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key: 'rowIndex',
        //   width: 60,
        //   align: 'center',
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1
        //   },
        // },
        {
          title: '库房绑定',
          align: 'center',
          dataIndex: 'depotBind_dictText',
          width: 80,
        },
        {
          title: '货架绑定',
          align: 'center',
          dataIndex: 'goodsBind_dictText',
          width: 80,
        },
        {
          title: '目录类型',
          align: 'center',
          dataIndex: 'kitName',
        },
        {
          title: '器材编号',
          align: 'center',
          dataIndex: 'wareId',
          width: 210,
        },
        {
          title: '包装类型',
          align: 'center',
          dataIndex: 'packType_dictText',
        },
        {
          title: '器材数量',
          align: 'center',
          dataIndex: 'wareNum',
        },
        {
          title: '盘点数量',
          align: 'center',
          dataIndex: 'checkNum',
        },
        {
          title: '盘盈盘亏',
          align: 'center',
          dataIndex: 'profitLess',
        },
        {
          title: '盘点时间',
          align: 'center',
          dataIndex: 'checkTime',
          fixed: 'right',
          width: 187,
        },
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align: 'center',
        //   fixed: 'right',
        //   width: 147,
        //   scopedSlots: { customRender: 'action' },
        // },
      ],
      url: {
        list: '/plForm/plForm/list',
        delete: '/plForm/plForm/delete',
        deleteBatch: '/plForm/plForm/deleteBatch',
        exportXlsUrl: '/plForm/plForm/exportXls',
        importExcelUrl: 'plForm/plForm/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  methods: {
    finish() {
      this.$emit('finish')
    },
    toOrderList() {
      this.$router.push('/storelist/storeList')
    },
    detailBtn() {
      this.visible = true
    },
    afterVisibleChange(val) {
      console.log('visible', val)
    },
    onClose() {
      this.visible = false
    },
    // 表格methods
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'depotBind', text: '库房绑定', dictCode: 'ware_depot,depot_name,id' })
      fieldList.push({ type: 'string', value: 'goodsBind', text: '货架绑定', dictCode: 'ware_goods,goods_id,id' })
      fieldList.push({ type: 'string', value: 'kitName', text: '目录类型' })
      fieldList.push({ type: 'string', value: 'wareId', text: '器材编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'packType', text: '包装类型', dictCode: 'ware_type' })
      fieldList.push({ type: 'BigDecimal', value: 'wareNum', text: '器材数量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'checkNum', text: '盘点数量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'profitLess', text: '盘盈盘亏', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'checkTime', text: '盘点时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
.information {
  line-height: 22px;

  .ant-row:not(:last-child) {
    margin-bottom: 24px;
  }
}
.money {
  font-family: 'Helvetica Neue', sans-serif;
  font-weight: 500;
  font-size: 20px;
  line-height: 14px;
}
</style>