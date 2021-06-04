<template>
  <a-card :bordered="false">
    <a-steps class="steps" :current="currentTab">
      <a-step title="选择盘点目标" />
      <a-step title="确认计划盘点单" />
      <a-step title="完成盘点" />
    </a-steps>
    <div class="content">
      <plan1 v-if="currentTab === 0" @nextStep="nextStep"/>
      <plan2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep"/>
      <plan3 v-if="currentTab === 2" @prevStep="prevStep" @finish="finish"/>
    </div>
  </a-card>
</template>

<script>
  import Plan1 from './Plan1'
  import Plan2 from './Plan2'
  import Plan3 from './Plan3'

  export default {
    name: "StepForm",
    components: {
      Plan1,
      Plan2,
      Plan3
    },
    data () {
      return {
        // description: '将一个冗长或用户不熟悉的表单任务分成多个步骤，指导用户完成。',
        currentTab: 0,

        // form
        form: null,
      }
    },
    methods: {

      // handler
      nextStep () {
        if (this.currentTab < 2) {
          this.currentTab += 1
        }
      },
      prevStep () {
        if (this.currentTab > 0) {
          this.currentTab -= 1
        }
      },
      finish () {
        this.currentTab = 0
      }
    }
  }
</script>

<style lang="less" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>