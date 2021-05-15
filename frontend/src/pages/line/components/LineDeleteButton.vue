<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINES, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";

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
        // console.log(this.line);
        await fetch(`http://localhost:8080/lines/${this.line.id}`, {
          method: 'DELETE'
        });

        // console.log(response);
        const response = await fetch("http://localhost:8080/lines");
        const lines = await response.json();
        this.setLines([...lines])
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
