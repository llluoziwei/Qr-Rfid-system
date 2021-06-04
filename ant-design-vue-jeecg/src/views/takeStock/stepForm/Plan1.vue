<template>
  <div>
    <a-form style="max-width: 500px; margin: 80px auto 0">
      <a-form-item label="请选择盘点目标" :labelCol="{ span: 5 }" :wrapperCol="{ span: 19 }">
        <!-- 选择库房 -->
        <div style="padding-left: 30px">
          <div :style="{ borderBottom: '1px solid #E9E9E9' }">
            <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange">
              选择全部
            </a-checkbox>
          </div>

          <a-checkbox-group v-model="checkedList" :options="plainOptions" @change="onChange" />
        </div>
      </a-form-item>
      <a-form-item :wrapperCol="{ span: 19, offset: 5 }">
        <div style="text-align: center; margin-top: 40px">
          <a-button type="primary" @click="nextStep"><span>生成盘点单</span></a-button>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
const plainOptions = ['库房1', '库房2', '库房3', '库房4']
const defaultCheckedList = ['库房1']
export default {
  name: 'Plan1',
  data() {
    return {
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