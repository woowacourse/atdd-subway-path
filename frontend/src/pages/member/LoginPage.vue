<template>
  <v-sheet class="d-flex flex-column justify-center mt-12">
    <div class="d-flex justify-center relative">
      <v-card class="card-border px-3 pt-3 pb-5" width="350">
        <v-form ref="loginForm" v-model="valid" @submit.prevent>
          <v-card-title class="font-weight-bold justify-center">
            로그인
          </v-card-title>
          <v-card-text class="px-4 pt-4 pb-0">
            <div class="d-flex">
              <v-text-field
                  v-model="member.email"
                  :rules="rules.member.email"
                  color="grey darken-1"
                  dense
                  label="이메일을 입력해주세요."
                  outlined
                  prepend-inner-icon="mdi-email"
              ></v-text-field>
            </div>
            <div class="d-flex mt-2">
              <v-text-field
                  v-model="member.password"
                  :rules="rules.member.password"
                  color="grey darken-1"
                  dense
                  label="비밀번호를 입력해주세요."
                  outlined
                  prepend-inner-icon="mdi-lock"
                  type="password"
              ></v-text-field>
            </div>
          </v-card-text>
          <v-card-actions class="px-4 pb-4">
            <v-spacer></v-spacer>
            <v-btn
                :disabled="!valid"
                class="w-100"
                color="amber"
                depressed
                @click.prevent="onLogin"
            >
              로그인
            </v-btn>
          </v-card-actions>
          <router-link class="d-flex justify-center" to="join">
            <span>아직 회원이 아니신가요?</span>
          </router-link>
        </v-form>
      </v-card>
    </div>
  </v-sheet>
</template>

<script>
import {mapGetters, mapMutations} from "vuex";
import {SET_MEMBER, SHOW_SNACKBAR} from "../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../utils/constants";
import validator from "../../utils/validator";

export default {
  name: "LoginPage",
  computed: {
    ...mapGetters(["accessToken"]),
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_MEMBER]),
    isValid() {
      return this.$refs.loginForm.validate();
    },
    async onLogin() {
      if (!this.isValid()) {
        return;
      }
      try {
        const {email, password} = this.member;
        await fetch("http://localhost:8080/login/token", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: email,
            password: password
          })
        }).then(function (response) {
          if (response.status == 401) {
            alert("로그인 문제 발생 / 아이디와 비밀번호를 다시 확인해주세요.");
          }
          if (response.ok) {
            alert("로그인 성공");
          }
          return response.json();
        }).then((data) => {
          localStorage.setItem("token", JSON.stringify(data));
        });

        await fetch("http://localhost:8080/members/me", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token")).accessToken
          }
        }).then(function (response) {
          if (!response.ok) {
            alert("회원이 아닙니다.");
            throw new Error("회원이 아닙니다.");
          }
          return response.json();
        }).then((data) => {
          console.log(data);
          this.setMember(data);
        })

        await this.$router.replace(`/`);
        this.showSnackbar(SNACKBAR_MESSAGES.LOGIN.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LOGIN.FAIL);
        throw new Error(e);
      }
    },
  },
  data() {
    return {
      valid: false,
      rules: {...validator},
      member: {
        email: "",
        password: "",
      },
    };
  },
};
</script>
