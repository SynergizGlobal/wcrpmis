export const dmsData = {
  "MUTP IIIA": {
    meta: {
      projectCode: "MUTP-3A"
    },

    // 🔹 Project-level folders (shown when ONLY project is selected)
    folders: [
      {
        name: "General Correspondence",
        type: "folder",
        children: [
          {
            name: "Intro Letter.pdf",
            type: "file",
            fileType: "pdf",
            size: "1.2 MB",
            uploadedOn: "12-01-2025"
          }
        ]
      }
    ],

    contracts: {
      "224 Construction of Important Bridges": {
        folders: [
          {
            name: "Approved Drawings",
            type: "folder",
            children: [
              {
                name: "GAD Bridge 73.pdf",
                type: "file",
                fileType: "pdf",
                size: "2.3 MB"
              },
              {
                name: "GAD Bridge 74.dwg",
                type: "file",
                fileType: "dwg",
                size: "8.1 MB"
              }
            ]
          },
          {
            name: "RA Bills",
            type: "folder",
            children: [
              {
                name: "RA Bill Jan.pdf",
                type: "file",
                fileType: "pdf"
              }
            ]
          }
        ]
      },

      // 🔹 SECOND contract (important for testing)
      "225 ROB Construction": {
        folders: [
          {
            name: "Reports",
            type: "folder",
            children: [
              {
                name: "Progress Report Feb.pdf",
                type: "file",
                fileType: "pdf"
              }
            ]
          }
        ]
      }
    }
  },

  // 🔹 SECOND project (critical)
  "MUTP IIIB": {
    folders: [
      {
        name: "Tender Documents",
        type: "folder",
        children: [
          {
            name: "Tender Notice.pdf",
            type: "file",
            fileType: "pdf"
          }
        ]
      }
    ],

    contracts: {
      "301 Track Doubling": {
        folders: [
          {
            name: "Design Drawings",
            type: "folder",
            children: []
          }
        ]
      }
    }
  }
};
