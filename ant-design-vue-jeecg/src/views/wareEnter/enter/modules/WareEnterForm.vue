<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="器材编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wareId', validatorRules.wareId]" placeholder="请输入器材编号" disabled ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="器材数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['wareNum', validatorRules.wareNum]" placeholder="请输入器材数量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="目录类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
  	          <j-tree-select
                ref="treeSelect"
                placeholder="请选择目录类型"
                v-decorator="['kitName', validatorRules.kitName]"
                dict="kit_catalog,kit_name,id"
                pidValue="0"
                dictCode="kitName"
                >
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="二维码绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['qrBind', validatorRules.qrBind]" placeholder="请输入二维码绑定"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="RFID绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['rfidBind', validatorRules.rfidBind]" placeholder="请输入RFID绑定"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="入库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择入库时间" v-decorator="['enterTime']" :trigger-change="true" style="width: 100%" disabled/>
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

  export default {
    name: 'WareEnterForm',
    components: {
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
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
            initialValue:'JCBT456451246',
            rules: [
              { required: true, message: '请输入器材编号!'},
            ]
          },
          wareNum: {
            initialValue:1,
            rules: [
              { required: true, message: '请输入器材数量!'},
            ]
          },
          kitName: {
            rules: [
              { required: true, message: '请输入目录类型!'},
            ]
          },
          qrBind: {
            initialValue:"",
            rules: [
            ]
          },
          rfidBind: {
            initialValue:"",
            rules: [
            ]
          },
        },
        url: {
          add: "/wareenter/wareEnter/add",
          edit: "/wareenter/wareEnter/edit",
          queryById: "/wareenter/wareEnter/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'wareId','wareNum','kitName','qrBind','rfidBind','enterTime'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'wareId','wareNum','kitName','qrBind','rfidBind','enterTime'))
      },
    }
  }
</script>