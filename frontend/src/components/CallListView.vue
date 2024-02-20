<template>

    <v-data-table
        :headers="headers"
        :items="callList"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'CallListView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            callList : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/callLists'))

            temp.data._embedded.callLists.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.callList = temp.data._embedded.callLists;
        },
        methods: {
        }
    }
</script>

