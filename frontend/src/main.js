import Vue from "vue";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import router from "./router";
import store from './store'
import {mapMutations} from "vuex";
import {SET_MEMBER, SHOW_SNACKBAR} from "@/store/shared/mutationTypes";

Vue.config.productionTip = false;

new Vue({
    vuetify,
    router,
    store,
    async created() {
        let getCookie = function (name) {
            let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
            return value ? value[2] : null;
        };
        const member = await fetch("/members/me", {
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + getCookie("JWT")
            }
        }).then(res => res.json())
            .then(data => {
                return {
                    id: data.id,
                    email: data.email,
                    age: data.age
                }
            });
        this.setMember(member);
    },
    methods: {
        ...mapMutations([SHOW_SNACKBAR, SET_MEMBER]),
    },
    render: h => h(App)
}).$mount("#app");
