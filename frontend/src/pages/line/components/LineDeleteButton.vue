<template>
  <v-btn @click="onDeleteLine" icon>
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
        // TODO Line을 삭제하는 API를 추가해주세요.(완료)
        const deleteRequest = {
          method: 'DELETE',
          headers: {
            'Authorization': 'Bearer' + this.$store.state.accessToken
          }
        }
        await fetch("/api/lines/" + this.line.id, deleteRequest);

        // await fetch("/api/lines/{id}")
        // TODO 전체 Line 데이터를 불러오는 API를 추가해주세요.(완료)
        // const lines = await fetch("/api/lines")
        // this.setLines([...lines])

        const finaRequest = {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer' + this.$store.state.accessToken
          }
        }
        const findResponse = await fetch("/api/lines", finaRequest);
        const lines = await findResponse.json();
        this.setLines([ ...lines]); //setLines는 데이터를 관리하기 위해 단 1개 존재하는 저장소에 노선 정보를 저장하는 메서드입니다.
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
