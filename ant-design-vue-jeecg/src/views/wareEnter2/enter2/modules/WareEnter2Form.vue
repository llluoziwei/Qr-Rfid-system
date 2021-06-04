<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="器材编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled v-decorator="['wareId',validatorRules.wareId]" placeholder="请输入器材编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="目录类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择目录类型"
                v-decorator="['kitName', validatorRules.kitName]"
                dict="kit_catalog2,kit_name,id"
                pidValue="0"
              >
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="器材数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number
                v-decorator="['wareNum', validatorRules.wareNum]"
                placeholder="请输入器材数量"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="二维码绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qrBind']" placeholder="请输入二维码绑定"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="RFID绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['rfidBind']" placeholder="请输入RFID绑定"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="入库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date
                placeholder="请选择入库时间"
                v-decorator="['enterTime']"
                :trigger-change="true"
                :show-time="true"
                date-format="yyyy-MM-DD HH:mm:ss"
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
import { httpAction, getAction, putAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JDateVue from '../../../../components/jeecg/JDate.vue'

export default {
  name: 'WareEnter2Form',
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
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
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
        wareId: {
          rules: [{ required: true, message: '请输入器材编号!' }],
        },
        wareNum: {
          initialValue: 1,
          rules: [{ required: true, message: '请输入器材数量!' }],
        },
        kitName: {
          rules: [{ required: true, message: '请输入目录类型!' }],
        },
        qrBind: {
          initialValue: '123',
          rules: [],
        },
        enterTime: {
          // rules: [{ required: true, message: '请输入入库时间!' }],
        },
      },
      url: {
        add: '/wareenter2/wareEnter2/add',
        edit: '/wareenter2/wareEnter2/edit',
        queryById: '/wareenter2/wareEnter2/queryById',
      },
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true;
        }
        return true
      }
      return this.disabled
    },
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
      this.edit({})
    },
    edit(record) {
      const url = 'sys/fillRule/executeRuleByCode/'
      const ruleCode = 'kit_num_code'
      const that = this
      var date = new Date(); //日期对象
      var now = "";
      now = date.getFullYear()+"-";
      now = now + (date.getMonth()+1)+"-";//取月的时候取的是当前月-1如果想取当前月+1就可以了
      now = now + (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())+" " ;
      now = now + (date.getHours() < 10 ? '0' + date.getHours() : date.getHours())+":";
      now = now + (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes())+":";
      now = now + (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())+"";
      // console.log(now)
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
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'wareId', 'wareNum', 'kitName', 'qrBind', 'rfidBind', 'enterTime'))
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
                that.$emit('ok',formData)
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
      this.form.setFieldsValue(pick(row, 'wareId', 'wareNum', 'kitName', 'qrBind', 'rfidBind', 'enterTime'))
    },
  },
}
</script>