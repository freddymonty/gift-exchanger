@param freddym.webportfolio.Model.User user
@param freddym.webportfolio.Model.Session session

@template.layouts.main(
title = "Edit Gift Exchange Session",
content = @`
<style>
    .editSession-main {
        min-height: 80vh;
        padding: 3rem;
        background-color: #F5F1E8;
        color: #3E2723;
    }

    .edit-card {
        width: 100%;
        max-width: 800px;
        margin: 0 auto;
        background-color: #fffaf0;
        border: 2px solid #8B7E47;
        border-radius: 16px;
        padding: 2rem;
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
    }

    .participant-row {
        display: flex;
        gap: 10px;
        margin-bottom: 10px;
    }

    .participant-row input {
        flex: 1;
    }

    .form-control {
        border: 1px solid #8B7E47;
    }

    .form-control:focus {
        border-color: #C9A227;
        box-shadow: 0 0 0 0.25rem rgba(201, 162, 39, 0.25);
    }

    .btn-add {
        background-color: #C9A227;
        border: none;
        color: #3E2723;
    }

    .btn-add:hover {
        background-color: #D4AF37;
        color: #3E2723;
    }

    .btn-remove {
        background-color: #8B7E47;
        border: none;
        color: #F5F1E8;
    }

    .btn-remove:hover {
        background-color: #6B5D33;
        color: #F5F1E8;
    }

    .btn-save {
        background-color: #2D6A4F;
        border: none;
        color: #F5F1E8;
    }

    .btn-save:hover {
        background-color: #1B4332;
        color: #F5F1E8;
    }
</style>

<script>
    function addParticipantField() {
        const container = document.getElementById("participantsContainer");

        const row = document.createElement("div");
        row.className = "participant-row";

        row.innerHTML = '<input type="hidden" name="participantIds" value="">'
            +'<input class="form-control" type="text" name="participantNames" placeholder="Participant Name" required>'
            +' <input class="form-control" type="tel" name="participantPhoneNumbers" placeholder="Phone Number" required>'
                ';'

                container.appendChild(row);
                }

                function removeParticipantField() {
                const container = document.getElementById("participantsContainer");
                const rows = container.getElementsByClassName("participant-row");

                if (rows.length > 1) {
                container.removeChild(rows[rows.length - 1]);
                }
                }
                </script>

                <div class="editSession-main">
                <div class="edit-card">
                <h1>Edit Gift Exchange Session</h1>

                <form method="post" action="/editSession/${session.getId()}">

                <div class="mb-4">
                <label class="form-label">Session Name</label>
                <input class="form-control"
                       type="text"
                       name="sessionName"
                       value="${session.getSessionName()}"
                       required>
                </div>

                <h3>Participants</h3>

                <div id="participantsContainer">
                @for(var participant : session.getParticipants())
            <div class="participant-row">
                <input type="hidden"
                       name="participantIds"
                       value="${participant.getId()}">

                    <input class="form-control"
                           type="text"
                           name="participantNames"
                           value="${participant.getName()}"
                           placeholder="Participant Name"
                           required>

                        <input class="form-control"
                               type="tel"
                               name="participantPhoneNumbers"
                               value="${participant.getPhoneNumber()}"
                               placeholder="Phone Number"
                               required>
            </div>
        @endfor
    </div>

        <div class="mt-3 d-flex gap-2">
            <button type="button"
                    class="btn btn-add"
                    onclick="addParticipantField()">
                <i class="bi bi-plus-circle"></i>
                Add Participant
            </button>

            <button type="button"
                    class="btn btn-remove"
                    onclick="removeParticipantField()">
                <i class="bi bi-dash-circle"></i>
                Remove Participant
            </button>
        </div>

        <div class="mt-4 d-flex gap-2">
            <button type="submit" class="btn btn-save">
                Save Changes
            </button>

            <a href="/executeSession/${session.getId()}" class="btn btn-secondary">
                Cancel
            </a>
        </div>

    </form>
    </div>
    </div>
        `
)