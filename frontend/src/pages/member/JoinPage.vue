<template>
  <v-sheet class="d-flex flex-column justify-center mt-12">
    <div class="d-flex justify-center relative">
      <v-card class="card-border px-3 pt-3 pb-5" width="350">
        <v-form ref="joinForm" v-model="valid" @submit.prevent>
          <v-card-title class="font-weight-bold justify-center">
            회원가입
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
                  v-model="member.age"
                  :rules="rules.member.age"
                  color="grey darken-1"
                  dense
                  label="나이를 입력해주세요."
                  outlined
                  prepend-inner-icon="mdi-account"
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
            <div class="d-flex mt-2">
              <v-text-field
                  v-model="member.confirmPassword"
                  :rules="[
                  (member.password &&
                    member.password === member.confirmPassword) ||
                    '비밀번호가 일치하지 않습니다.',
                ]"
                  color="grey darken-1"
                  dense
                  label="비밀번호를 한번 더 입력해주세요."
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
                class="width-100"
                color="amber"
                depressed
                @click.prevent="onCreateMember"
            >
              회원가입
            </v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </div>
  </v-sheet>
</template>

<script>
import {mapMutations} from "vuex";
import {SHOW_SNACKBAR} from "../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../utils/constants";
import validator from "../../utils/validator";

export default {
  name: "JoinPage",
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    isValid() {
      return this.$refs.joinForm.validate();
    },
    async onCreateMember() {
      if (!this.isValid()) {
        return;
      }
      try {
        const {email, age, password} = this.member;
        await fetch("/api/members", {
          method: "POST",
          body: JSON.stringify({email, password, age}),
          headers: {"Content-Type": "application/json"}
        });

        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
        await this.$router.replace(`/login`);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
      }
    },
  },
  data() {
    return {
      valid: false,
      rules: {...validator},
      member: {
        email: "",
        age: "",
        password: "",
        confirmPassword: "",
      },
    };
  },
};
</script>
