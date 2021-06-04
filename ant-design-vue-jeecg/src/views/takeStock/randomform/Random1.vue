<template>
  <div>
    <a-form style="max-width: 500px; margin: 80px auto 0">
      <a-form-item label="生成盘点目标" :labelCol="{ span: 5 }" :wrapperCol="{ span: 19 }">
        <!-- 选择仓库 -->
        <div style="padding-left:30px">
          <div :style="{ borderBottom: '1px solid #E9E9E9' }">
            <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange" :disabled="disabled">
              选择全部
            </a-checkbox>
          </div>

          <a-checkbox-group v-model="checkedList" :options="plainOptions" @change="onChange" :disabled="disabled"/>
        </div>
      </a-form-item>
      <a-form-item :wrapperCol="{ span: 19, offset: 5 }">
        <div style="text-align: center; margin-top: 40px">
          <a-button type="primary" @click="nextStep">生成盘点单</a-button>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
const plainOptions = ['库房1', '库房2', '库房3','库房4']
const defaultCheckedList = ['库房1', '库房2']
export default {
  name: 'Random1',
  data() {
    return {
      // 多选框数据
      disabled: true,
      checkedList: defaultCheckedList,
      indeterminate: true,
      checkAll: false,
      plainOptions,
    }
  },
  methods: {
    nextStep() {
      this.$emit('nextStep')
    },
    // 多选框选择
    onChange(checkedList) {
      this.indeterminate = !!checkedList.length && checkedList.length < plainOptions.length
      this.checkAll = checkedList.length === plainOptions.length
    },
    onCheckAllChange(e) {
      Object.assign(this, {
        checkedList: e.target.checked ? plainOptions : [],
        indeterminate: false,
        checkAll: e.target.checked,
      })
    },
  },
}
</script>

<style scoped>
</style>