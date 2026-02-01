<!-- UserRegisterPage.vue -->
<template>
  <div class="register-container">
    <div class="card">
      <div class="header">
        <h1 class="app-name">AI项目生成器</h1>
        <p class="subtitle">开启您的无代码开发之旅</p>
      </div>

      <div class="form-section">
        <h2 class="form-title">用户注册</h2>

        <form class="register-form" @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="userAccount">账号</label>
            <input
              id="userAccount"
              v-model="formState.userAccount"
              type="text"
              placeholder="请输入账号"
              class="form-input"
            />
            <div v-if="errors.userAccount" class="error-message">{{ errors.userAccount }}</div>
          </div>

          <div class="form-group">
            <label for="userPassword">密码</label>
            <div class="password-wrapper">
              <input
                id="userPassword"
                v-model="formState.userPassword"
                :type="showPassword.userPassword ? 'text' : 'password'"
                placeholder="请输入密码（至少8位）"
                class="form-input"
              />
              <button
                type="button"
                class="password-toggle"
                @click="togglePassword('userPassword')"
              >
                {{ showPassword.userPassword ? '隐藏' : '显示' }}
              </button>
            </div>
            <div v-if="errors.userPassword" class="error-message">{{ errors.userPassword }}</div>
          </div>

          <div class="form-group">
            <label for="checkPassword">确认密码</label>
            <div class="password-wrapper">
              <input
                id="checkPassword"
                v-model="formState.checkPassword"
                :type="showPassword.checkPassword ? 'text' : 'password'"
                placeholder="请再次输入密码"
                class="form-input"
              />
              <button
                type="button"
                class="password-toggle"
                @click="togglePassword('checkPassword')"
              >
                {{ showPassword.checkPassword ? '隐藏' : '显示' }}
              </button>
            </div>
            <div v-if="errors.checkPassword" class="error-message">{{ errors.checkPassword }}</div>
          </div>

          <div class="form-actions">
            <button type="submit" class="submit-btn" :disabled="loading">
              {{ loading ? '注册中...' : '注册' }}
            </button>
          </div>

          <div class="form-footer">
            <span>已有账号？</span>
            <RouterLink to="/user/login" class="link">立即登录</RouterLink>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/userController.ts'
import type { API } from '@/api/types'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const showPassword = reactive({
  userPassword: false,
  checkPassword: false
})

const errors = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

const loading = ref(false)

const togglePassword = (field: 'userPassword' | 'checkPassword') => {
  showPassword[field] = !showPassword[field]
}

const validateForm = () => {
  let isValid = true

  if (!formState.userAccount.trim()) {
    errors.userAccount = '请输入账号'
    isValid = false
  } else {
    errors.userAccount = ''
  }

  if (!formState.userPassword) {
    errors.userPassword = '请输入密码'
    isValid = false
  } else if (formState.userPassword.length < 8) {
    errors.userPassword = '密码长度不能小于8位'
    isValid = false
  } else {
    errors.userPassword = ''
  }

  if (!formState.checkPassword) {
    errors.checkPassword = '请确认密码'
    isValid = false
  } else if (formState.checkPassword !== formState.userPassword) {
    errors.checkPassword = '两次输入密码不一致'
    isValid = false
  } else {
    errors.checkPassword = ''
  }

  return isValid
}

const handleSubmit = async () => {
  if (!validateForm()) return

  loading.value = true
  try {
    const res = await userRegister(formState)

    if (res.data.code === 0) {
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      errors.userAccount = res.data.message || '注册失败'
    }
  } catch (error) {
    errors.userAccount = '注册请求失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
  overflow: hidden;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px 30px;
  text-align: center;
}

.app-name {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 10px 0;
}

.subtitle {
  opacity: 0.9;
  font-size: 14px;
  margin: 0;
}

.form-section {
  padding: 40px 30px;
}

.form-title {
  font-size: 24px;
  font-weight: 500;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.password-wrapper {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #667eea;
  font-size: 12px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.password-toggle:hover {
  background: rgba(102, 126, 234, 0.1);
}

.error-message {
  color: #ff4757;
  font-size: 12px;
  margin-top: 6px;
}

.form-actions {
  margin-top: 30px;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.2);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-footer {
  margin-top: 24px;
  text-align: center;
  color: #666;
  font-size: 14px;
}

.link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  margin-left: 5px;
}

.link:hover {
  text-decoration: underline;
}
</style>
