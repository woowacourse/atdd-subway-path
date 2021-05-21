import Vue from "vue";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import router from "./router";
import store from './store';
import {mapMutations} from "vuex";
import {SET_MEMBER} from "@/store/shared/mutationTypes";

Vue.config.productionTip = false;

new Vue({
  vuetify,
  router,
  store,
  async created() {
    const accessToken = localStorage.getItem("token");
    if (accessToken) {
      const member = await fetch("http://localhost:8080/members/me", {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + accessToken
        }
      });
      if (member.ok) {
        const memberInfo = await member.json();
        this.setMember(memberInfo);
      }
    }
  },
  methods: {
    ...mapMutations([SET_MEMBER])
  },
  render: h => h(App)
}).$mount("#app");
