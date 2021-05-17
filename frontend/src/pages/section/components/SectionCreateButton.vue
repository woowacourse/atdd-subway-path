<template>
  <Dialog :close="close">
    <template slot="trigger">
      <v-btn
          class="mx-2 section-create-button"
          color="amber"
          depressed
          fab
          @click="initLineView"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
    <template slot="title">
      <div class="width-100 text-center mt-6">구간 추가</div>
    </template>
    <template slot="text">
      <v-form ref="sectionForm" v-model="valid" @submit.prevent>
        <v-select
            v-model="sectionForm.lineId"
            :items="lineNameViews"
            color="grey darken-1"
            dense
            item-color="amber darken-3"
            label="노선 선택"
            outlined
            width="400"
            @change="onChangeLine"
        ></v-select>
        <div class="d-flex">
          <v-select
              v-model="sectionForm.upStationId"
              :items="allStationsView"
              class="pr-5"
              color="grey darken-1"
              dense
              item-color="amber darken-3"
              label="상행역"
              outlined
              width="400"
          ></v-select>
          <v-select
              v-model="sectionForm.downStationId"
              :items="allStationsView"
              class="pl-5"
              color="grey darken-1"
              dense
              item-color="amber darken-3"
              label="하행역"
              outlined
              width="400"
          ></v-select>
        </div>
        <div class="d-flex">
          <v-text-field
              v-model="sectionForm.distance"
              :rules="rules.section.distance"
              color="grey darken-1"
              label="거리"
              outlined
              placeholder="거리"
          ></v-text-field>
        </div>
      </v-form>
    </template>
    <template slot="action">
      <v-btn
          :disabled="!valid"
          color="amber"
          depressed
          @click.prevent="onCreateSection"
      >확인
      </v-btn
      >
    </template>
  </Dialog>
</template>

<script>
import {mapGetters, mapMutations} from "vuex";
import dialog from "../../../mixins/dialog";
import Dialog from "../../../components/dialogs/Dialog";
import {SET_LINE, SET_LINES, SHOW_SNACKBAR,} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";
import validator from "../../../utils/validator";

export default {
  name: "SectionCreateButton",
  components: {Dialog},
  mixins: [dialog],
  computed: {
    ...mapGetters(["lines", "stations"]),
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES, SET_LINE]),
    initLineView() {
      if (this.lines.length < 1) {
        return;
      }
      this.lineNameViews = this.lines.map(({name, id}) => {
        return {
          text: name,
          value: id,
        };
      });
    },
    async initLineStationsView() {
      try {
        const response = await fetch(`/api/lines/${this.sectionForm.lineId}`)

        if (!response.ok) {
          throw new Error(`${response.status}`);
        }

        this.selectedLine = await response.json();


        if (this.selectedLine.stations?.length < 1) {
          return;
        }
        this.lineStationsNameViews = this.selectedLine.stations?.map(
            (station) => {
              return {
                text: station.name,
                value: station.id,
              };
            }
        );
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
    async initAllStationsView() {
      try {
        if (this.stations.length < 1) {
          return;
        }
        this.allStationsView = this.stations.map(({name, id}) => {
          return {
            text: name,
            value: id,
          };
        });
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
    isValid() {
      return this.$refs.sectionForm.validate();
    },
    onChangeLine() {
      this.initLineStationsView();
      this.initAllStationsView();
    },
    async onCreateSection() {
      if (!this.isValid()) {
        return;
      }
      try {
        const lineId = this.selectedLine.id
        const {upStationId, downStationId, distance} = this.sectionForm
        const addResponse = await fetch(`/api/lines/${lineId}/sections`, {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({upStationId, downStationId, distance})
        });
        if (!addResponse.ok) {
          throw new Error(`${addResponse.status}`);
        }

        const lines = await fetch("/api/lines").then(data => data.json());
        this.setLines([...lines])
        const line = this.lines.find(({id}) => id === this.selectedLine.id);
        this.setLine(line);
        this.$refs.sectionForm.resetValidation();
        this.initSectionForm();
        this.closeDialog();
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
    initSectionForm() {
      this.sectionForm = {
        lineId: "",
        upStationId: "",
        downStationId: "",
        distance: "",
      };
    },
  },
  data() {
    return {
      rules: {...validator},
      sectionForm: {
        lineId: "",
        upStationId: "",
        downStationId: "",
        distance: "",
      },
      selectedLine: {},
      lineStationsNameViews: [],
      allStationsView: [],
      lineNameViews: [],
      valid: false,
    };
  },
};
</script>

<style lange="scss" scoped>
.section-create-button {
  top: -29px;
}
</style>
