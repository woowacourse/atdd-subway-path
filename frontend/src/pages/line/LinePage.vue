<template>
  <v-sheet class="d-flex flex-column justify-center mt-12">
    <div class="d-flex justify-center relative">
      <v-card class="card-border" max-height="600" max-width="600" width="500">
        <v-card-title class="font-weight-bold justify-center relative">
          노선 관리
        </v-card-title>
        <v-card-text class="relative px-0 pb-0 mb-6 d-flex flex-column">
          <v-divider/>
          <div class="d-flex justify-end mr-4 line-create-button-container">
            <LineCreateButton/>
          </div>
          <div class="overflow-y-auto">
            <v-list-item-group color="grey darken-3">
              <v-list-item v-for="line in lines" :key="line.id">
                <v-list-item-content>
                  <v-list-item-title>
                    <v-avatar
                        :color="line.color"
                        class="relative bottom-1"
                        left
                        size="10"
                    />
                    <span class="ml-2">{{ line.name }}</span>
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-action class="flex-row">
                  <LineEditButton :line="line"/>
                  <LineDeleteButton :line="line"/>
                </v-list-item-action>
              </v-list-item>
            </v-list-item-group>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </v-sheet>
</template>

<script>
import LineCreateButton from "./components/LineCreateButton";
import {SET_LINES, SET_STATIONS} from "../../store/shared/mutationTypes";
import {mapGetters, mapMutations} from "vuex";
import LineEditButton from "./components/LineEditButton";
import LineDeleteButton from "./components/LineDeleteButton";

export default {
  name: "LinePage",
  components: {LineDeleteButton, LineEditButton, LineCreateButton},
  computed: {
    ...mapGetters(["lines"]),
  },
  async created() {
    const stationResponse = await fetch("/api/stations")
    const stations = await stationResponse.json()
    this.setStations([...stations])
    const lineResponse = await fetch("/api/lines")
    const lines = await lineResponse.json()
    this.setLines([...lines])
  },
  methods: {
    ...mapMutations([SET_LINES, SET_STATIONS]),
  },
};
</script>

<style lang="scss" scoped>
.line-create-button-container {
  height: 25px;
}
</style>
