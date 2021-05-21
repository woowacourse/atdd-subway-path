<template>
  <v-btn icon @click="onDeleteLine">
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "LineDeleteButton",
  props: {
    line: {
      type: Object,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES]),
    async onDeleteLine() {
      try {
        let getCookie = function (name) {
          let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
          return value ? value[2] : null;
        };
        await fetch("http://localhost:8080/lines/" + this.line.id, {
          method: 'DELETE'
        })
        const lines = await fetch("http://localhost:8080/lines", {
          headers: {
            "Authorization": "Bearer " + getCookie("JWT")
          }
        })
            .then(res => res.json())
            .then(data => {
              return data;
            });
        this.setLines([...lines])
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
