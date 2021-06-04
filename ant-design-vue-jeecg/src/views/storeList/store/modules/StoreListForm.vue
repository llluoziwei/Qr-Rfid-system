<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container>
      <!-- :disabled="formDisabled" -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="器材编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input
                v-decorator="['wareId', validatorRules.wareId]"
                placeholder="请输入器材编号"
                disabled
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="目录类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择目录类型"
                v-decorator="['kitName', validatorRules.kitName]"
                dict="kit_catalog2,kit_name,id"
                pidValue="0"
                :disabled="disabled3"
              >
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="包装类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag
                type="list"
                v-decorator="['packType', validatorRules.packType]"
                :trigger-change="true"
                dictCode="ware_type"
                placeholder="请选择包装类型"
                :disabled="disabled3"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="器材数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number
                v-decorator="['wareNum', validatorRules.wareNum]"
                placeholder="请输入器材数量"
                style="width: 100%"
                :disabled="disabled3"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="库房绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag
                type="list"
                v-decorator="['depotBind']"
                :trigger-change="true"
                dictCode="ware_depot,depot_name,id"
                placeholder="请选择库房绑定"
                :disabled="disabled3"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="绑定货架" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!-- <j-search-select-tag v-decorator="['goodsBind']" dict="ware_goods,goods_id,id" /> -->
              <j-multi-select-tag type="list_multi" v-decorator="['goodsBind']" :trigger-change="true" dictCode="ware_goods,goods_id,id" placeholder="请选择货架绑定" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="二维码绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qrBind']" placeholder="请输入二维码绑定"></a-input>
              <a-button type="primary">切换绑定</a-button>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="RFID绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['rfidBind']" placeholder="请输入RFID绑定"></a-input>
              <a-button type="primary" @click="showModal">切换绑定</a-button>
              <a-modal
                title="RFID标签绑定"
                :visible="rfidvisible"
                :confirm-loading="confirmLoading"
                @ok="handleOk"
                @cancel="handleCancel"
              >
                <a-form-item label="请用RFID扫码设备对要绑定的标签进行扫码">
                  <a-input placeholder="RFID编号" />
                </a-form-item>
              </a-modal>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="入库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date
                placeholder="请选择入库时间"
                v-decorator="['enterTime']"
                :trigger-change="true"
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                disabled
              />
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import { putAction } from '../../../../api/manage'

export default {
  name: 'StoreListForm',
  components: {},
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    // //表单禁用
    // disabled: {
    //   type: Boolean,
    //   default: false,
    //   required: false
    // }
  },
  data() {
    return {
      // 绑定标签start
      ModalText: 'Content of the modal',
      rfidvisible: false,
      confirmLoading: false,
      // 绑定标签end
      disabled3: false,
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        wareNum: {
          initialValue: 1,
          rules: [],
        },
        wareId: {
          // initialValue: 123123,
          rules: [{ required: true, message: '请输入器材编号!' }],
        },
        kitName: {
          rules: [{ required: true, message: '请选择目录类型!' }],
        },
        packType: {
          rules: [{ required: true, message: '请选择包装类型!' }],
        },
      },
      url: {
        add: '/storelist/storeList/add',
        edit: '/storelist/storeList/edit',
        queryById: '/storelist/storeList/queryById',
      },
    }
  },
  computed: {
    // formDisabled() {
    //   if (this.formBpm === true) {
    //     if (this.formData.disabled === false) {
    //       return false
    //     }
    //     return true
    //   }
    //   return this.disabled
    // },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    add() {
      this.disabled3 = false
      // this.edit({})
      // 后端生成规则接口调用
      const url = 'sys/fillRule/executeRuleByCode/'
      const ruleCode = 'kit_num_code'
      const that = this
      var date = new Date()
      var now = ''
      now = date.getFullYear() + '-'
      now = now + (date.getMonth()+1) + '-' //取月的时候取的是当前月-1如果想取当前月+1就可以了
      now = now + (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
      now = now + (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
      now = now + (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
      now = now + (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()) + ''
      console.log(date.getMonth()+1)
      putAction(url + ruleCode, {}).then((res) => {
        if (res.success) {
          that.model.settlementNo = res.result

          that.$nextTick(() => {
            that.form.setFieldsValue({
              wareId: res.result,
              enterTime: now,
            })
          })
        }
      })
    },
    edit(record) {
      this.disabled3 = true
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'wareId',
            'kitName',
            'packType',
            'wareNum',
            'depotBind',
            'goodsBind',
            'qrBind',
            'rfidBind',
            'enterTime'
          )
        )
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          'wareId',
          'kitName',
          'packType',
          'wareNum',
          'depotBind',
          'goodsBind',
          'qrBind',
          'rfidBind',
          'enterTime'
        )
      )
    },
    // 绑定标签
    showModal() {
      this.rfidvisible = true
    },
    handleOk(e) {
      this.ModalText = 'The modal will be closed after two seconds'
      this.confirmLoading = true
      setTimeout(() => {
        this.rfidvisible = false
        this.confirmLoading = false
      }, 2000)
    },
    handleCancel(e) {
      console.log('Clicked cancel button')
      this.rfidvisible = false
    },
  },
}
</script>