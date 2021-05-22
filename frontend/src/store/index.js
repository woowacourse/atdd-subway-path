import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from 'vuex-persistedstate';
import station from "./modules/station";
import line from "./modules/line";
import member from "./modules/member";
import auth from "./modules/auth";
import snackbar from "./modules/snackbar";

const modules = {
    snackbar,
    station,
    line,
    member,
    auth,
}

const plugins = [
    createPersistedState(),
];

Vue.use(Vuex);

export default new Vuex.Store({
    modules: modules,
    plugins,
});
