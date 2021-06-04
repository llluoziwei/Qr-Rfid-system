<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="库房绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['depotBind']" :trigger-change="true" dictCode="ware_depot,depot_name,id" placeholder="请选择库房绑定" />
        </a-form-item>
        <a-form-item label="货架绑定" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsBind']" :trigger-change="true" dictCode="ware_goods,goods_id,id" placeholder="请选择货架绑定" />
        </a-form-item>
        <a-form-item label="目录类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
	          <j-tree-select
              ref="treeSelect"
              placeholder="请选择目录类型"
              v-decorator="['kitName']"
              dict="kit_catalog2,kit_name,id"
              pidValue="0"
              >
            </j-tree-select>
        </a-form-item>
        <a-form-item label="器材编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['wareId']" placeholder="请输入器材编号" ></a-input>
        </a-form-item>
        <a-form-item label="包装类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['packType']" :trigger-change="true" dictCode="ware_type" placeholder="请选择包装类型" />
        </a-form-item>
        <a-form-item label="器材数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['wareNum']" placeholder="请输入器材数量" style="width: 100%" />
        </a-form-item>
        <a-form-item label="盘点数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['checkNum']" placeholder="请输入盘点数量" style="width: 100%" />
        </a-form-item>
        <a-form-item label="盘盈盘亏" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['profitLess']" placeholder="请输入盘盈盘亏" style="width: 100%" />
        </a-form-item>
        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择父级节点"
            v-decorator="['pid']"
            dict="plan_check,depot_bind,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child"
            >
          </j-tree-select>
        </a-form-item>
        
      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  export default {
    name: "PlanCheckModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
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
        },
        url: {
          add: "/plancheck/planCheck/add",
          edit: "/plancheck/planCheck/edit",
        },
        expandedRowKeys:[],
        pidField:"pid"
     
      }
    },
    created () {
    },
    methods: {
      add (obj) {
        this.edit(obj);
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'depotBind','goodsBind','kitName','wareId','packType','wareNum','checkNum','profitLess','pid'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values);
            let new_pid = this.model[this.pidField]
             if(this.model.id && this.model.id === new_pid){
              that.$message.warning("父级节点不能选择自己");
              that.confirmLoading = false;
              return;
            }
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                this.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'depotBind','goodsBind','kitName','wareId','packType','wareNum','checkNum','profitLess','pid'))
      },
      submitSuccess(formData,flag){
        if(!formData.id){
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys=[]
          this.getExpandKeysByPid(formData[this.pidField],treeData,treeData)
          this.$emit('ok',formData,this.expandedRowKeys.reverse());
        }else{
          this.$emit('ok',formData,flag);
        }
      },
      getExpandKeysByPid(pid,arr,all){
        if(pid && arr && arr.length>0){
          for(let i=0;i<arr.length;i++){
            if(arr[i].key==pid){
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>