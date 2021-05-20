<template>
  <v-app-bar :height="65" app clipped-left color="amber" flat>
    <v-toolbar-title class="pl-0 mr-12 align-center relative bottom-2">
      <v-btn text to="/">
        <div class="title">
          <span>RUNNINGMAP</span>
        </div>
      </v-btn>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn
        v-for="navItem in navItems"
        :key="navItem._id"
        :to="navItem.link"
        text
    >{{ navItem.text }}
    </v-btn
    >
    <template v-if="member">
      <v-menu bottom class="z-10" offset-y transition="slide-y-transition">
        <template v-slot:activator="{ on, attrs }">
          <div v-bind="attrs" v-on="on">
            <div class="text-normal cursor-pointer mx-2 my-thumbnail-button">
              <v-avatar dark height="35" width="35">
                <img
                    src="https://avatars3.githubusercontent.com/u/4353846?v&amp;#x3D;4"
                />
              </v-avatar>
              <div class="desktop-view d-inline-block">
                <span>{{ member.email.split("@")[0] }}</span>
                <v-icon class="font-size-10" right>ti-angle-down</v-icon>
              </div>
            </div>
          </div>
        </template>
        <v-list class="py-0">
          <v-list class="py-0">
            <MyPageButton/>
            <v-divider class="ma-0"/>
            <LogoutButton/>
          </v-list>
        </v-list>
      </v-menu>
    </template>
    <template v-else>
      <router-link to="/login">
        <v-btn text>로그인</v-btn>
      </router-link>
    </template>
  </v-app-bar>
</template>

<script>
import {NAV_ITEMS} from "../../utils/constants";
import {mapGetters} from "vuex";
import LogoutButton from "./components/LogoutButton";
import MyPageButton from "./components/MyPageButton";

export default {
  name: "Header",
  components: {MyPageButton, LogoutButton},
  computed: {
    ...mapGetters(["member"]),
  },
  data() {
    return {
      navItems: [...NAV_ITEMS],
    };
  },
};
</script>

<style lang="scss" scoped>
.header-logo {
  width: 25px;
}
</style>
