@param freddym.webportfolio.Model.User user

@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
<style>
    .createSession-main {
        min-height: 80vh;
        padding: 3rem;
        background-color: #F5F1E8;
        color: #3E2723;
    }

    .session-card {
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
    }

    .btn-create {
        background-color: #2D6A4F;
        border: none;
    }

    .btn-create:hover {
        background-color: #1B4332;
    }
</style>
<%--    <script>--%>
<%--        function addParticipantField() {--%>
<%--            const container = document.getElementById("participantsContainer");--%>

<%--            const row = document.createElement("div");--%>
<%--            row.className = "participant-row";--%>

<%--            row.innerHTML = '' +--%>
<%--                '<input class="form-control" type="text" name="participantNames" placeholder="Name" required>' +--%>
<%--                '<input class="form-control" type="tel" name="participantPhoneNumbers" placeholder="Phone Number" required>';--%>
<%--            container.appendChild(row);--%>
<%--                        }--%>

<%--        function removeParticipantField() {--%>
<%--            const container = document.getElementById("participantsContainer");--%>
<%--            const rows = container.getElementsByClassName("participant-row");--%>
<%--            if(rows.length > 1) {--%>
<%--                container.removeChild(rows[rows.length - 1]);--%>
<%--            }--%>
<%--        }--%>
<%--    </script>--%>

    <div class="createSession-main">
        <div class="session-card">

            <h1>Create Gift Exchange Session</h1>

            <p>
                Create your gift exchange session and then share the registration link with everyone joining the session.
            </p>

            <form method="post" action="/createNewSession">

                <div class="mb-4">
                    <label class="form-label">Session Name</label>
                    <label>
                        <input class="form-control"
                               type="text"
                               name="sessionName"
                               placeholder="Session Name" required>
                    </label>

                </div>


                <div class="mt-4">
                    <button type="submit"
                            class="btn btn-create text-light">
                        Create Session
                    </button>
                </div>

            </form>

            <div class="info-box">
                <strong>Next Steps:</strong>
                <p>
                    After creating this session, you will receive a participant registration link. Each participant enters their own information and chooses whether to receive their gift exchange assignment by SMS.
                </p>
            </div>
        </div>
    </div>

`
)