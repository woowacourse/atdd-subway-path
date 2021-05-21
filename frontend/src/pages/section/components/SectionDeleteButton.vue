<template>
  <v-btn icon @click="onDeleteLine">
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINE, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "SectionDeleteButton",
  props: {
    lineId: {
      type: String,
      required: true,
    },
    stationId: {
      type: String,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINE]),
    async onDeleteLine() {
      try {
        let getCookie = function (name) {
          let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
          return value ? value[2] : null;
        };
        await fetch("http://localhost:8080/lines/" + this.lineId + "/sections?stationId=" + this.stationId, {
          method: 'DELETE',
          headers: {
            "Authorization": "Bearer " + getCookie("JWT")
          }
        });

        const line = await fetch('http://localhost:8080/lines/' + this.lineId)
            .then(res => res.json())
            .then(data => {
              return data;
            });
        this.setLine({ ...line });
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
