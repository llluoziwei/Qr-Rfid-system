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
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="包装类型">
              <j-dict-select-tag placeholder="请选择包装类型" v-model="queryParam.packType" dictCode="ware_type" />
            </a-form-item>
          </a-col>
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="目录类型">
              <a-input placeholder="请输入目录类型" v-model="queryParam.kitName"></a-input>
            </a-form-item>
          </a-col> -->
          <template v-if="toggleSearchStatus">
            <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="包装类型">
                <j-dict-select-tag placeholder="请选择包装类型" v-model="queryParam.packType" dictCode="ware_type"/>
              </a-form-item>
            </a-col> -->
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="库房绑定">
                <j-dict-select-tag
                  placeholder="请选择库房绑定"
                  v-model="queryParam.depotBind"
                  dictCode="ware_depot,depot_name,id"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="货架绑定">
                <j-search-select-tag
                  placeholder="请选择货架绑定"
                  v-model="queryParam.goodsBind"
                  dict="ware_goods,goods_id,id"
                />
              </a-form-item>
            </a-col>
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
      <a-button type="primary" icon="download" @click="handleExportXls('存放管理')">导出</a-button>
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
      <!-- <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
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
          <a @click="handleEdit(record)">分配/绑定</a>

          <!-- <a-divider type="vertical" />
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
          </a-dropdown> -->
        </span>
      </a-table>
    </div>

    <store-list-modal ref="modalForm" @ok="modalFormOk"></store-list-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import StoreListModal from './modules/StoreListModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
// import api
import { getAction } from '@api/manage'

export default {
  name: 'StoreListList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    StoreListModal,
  },
  data() {
    return {
      description: '存放管理管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
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
          dataIndex: 'wareId',
        },
        {
          title: '目录类型',
          align: 'center',
          dataIndex: 'kitName_dictText',
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
          title: '库房绑定',
          align: 'center',
          dataIndex: 'depotBind_dictText',
        },
        {
          title: '货架绑定',
          align: 'center',
          dataIndex: 'goodsBind_dictText',
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
        list: '/storelist/storeList/list',

        delete: '/storelist/storeList/delete',
        deleteBatch: '/storelist/storeList/deleteBatch',
        exportXlsUrl: '/storelist/storeList/exportXls',
        importExcelUrl: 'storelist/storeList/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      msg: '默认值',

      newWareE: [], //入库记录中的数据
      arr: [], //将存入库存管理的记录值
      dataSource: [], // new data
    }
  },
  mounted () {
        this.getOtherData()
  },
  created() {
    this.getSuperFieldList()

  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    // 获取入库记录与器材目录过滤后的数据
    getOtherData() {
      let urlEnt = '/wareenter2/wareEnter2/list' //入库接口
      let urlKit = '/kitcatalog2/kitCatalog2/childList' //目录接口
      //获取入库数据
      getAction(urlEnt).then((res) => {
        if (res.success) {
          this.newWareE = res.result.records
          return this.newWareE
        }
      })
      //获取器材目录数据
      getAction(urlKit).then((res) => {
        if (res.success) {
          let that = this
          let children = res.result.records.filter((c) => c.hasChild == 0) //子
          let o = children.concat(that.newWareE)
          // console.log(o)
          for (let i in o) {
            for (let j in o) {
              if (o[i].id == o[j].kitName) {
                // console.log( o[i],o[j])
                let obj2 = {
                  //将遍历判断出来的数据保存到新对象中
                  createBy: o[i].createBy,
                  createTime: o[i].createTime,
                  depotBind: o[i].depotBind,
                  depotBind_dictText: o[i].depotBind_dictText,
                  enterTime: o[j].enterTime,
                  goodsBind: o[i].goodsBind,
                  goodsBind_dictText: o[i].goodsBind_dictText,
                  id: o[i].id,
                  kitName: o[j].kitName,
                  kitName_dictText: o[j].kitName_dictText,
                  packType: o[i].packType,
                  packType_dictText: o[i].packType_dictText,
                  qrBind: o[j].qrBind,
                  rfidBind: o[j].rfidBind,
                  sysOrgCode: o[i].sysOrgCode,
                  updateBy: o[i].updateBy,
                  updateTime: o[i].updateTime,
                  wareId: o[j].wareId,
                  wareNum: o[j].wareNum,
                }
                that.arr.splice(-1, 0, obj2) //push到arr
                // console.log(obj2);
              }
            }
          }
          // console.log(that.arr);
          this.changeData()
        }
      })
    },
    // 修改库存调库目录中的数据
    changeData() {
      this.dataSource.map((item) => {
        this.arr.push(item);
      })
      this.dataSource = this.arr;
      console.log(this.arr);
      // console.log(this.dataSource);
    },
    getDatas(val) {
      console.log(`enter2传递过来的数据: ${val}`) // hello?
    },
    initDictConfig() {},
    // 高级查询
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'wareId', text: '器材编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kitName', text: '目录类型' })
      fieldList.push({ type: 'string', value: 'packType', text: '包装类型', dictCode: 'ware_type' })
      fieldList.push({ type: 'BigDecimal', value: 'wareNum', text: '器材数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'depotBind', text: '库房绑定', dictCode: 'ware_depot,depot_name,id' })
      fieldList.push({
        type: 'sel_search',
        value: 'goodsBind',
        text: '货架绑定',
        dictTable: 'ware_goods',
        dictText: 'goods_id',
        dictCode: 'id',
      })
      fieldList.push({ type: 'string', value: 'qrBind', text: '二维码绑定', dictCode: '' })
      fieldList.push({ type: 'string', value: 'rfidBind', text: 'RFID绑定', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'enterTime', text: '入库时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>