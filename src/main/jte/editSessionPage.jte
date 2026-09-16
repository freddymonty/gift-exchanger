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

        .form-control {
            border: 1px solid #8B7E47;
        }

        .form-control:focus {
            border-color: #C9A227;
            box-shadow: 0 0 0 0.25rem rgba(201, 162, 39, 0.25);
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

        .btn-remove {
            background-color: #8B7E47;
            border: none;
            color: #F5F1E8;
        }

        .btn-remove:hover {
            background-color: #6B5D33;
            color: #F5F1E8;
        }
    </style>

    <div class="editSession-main">
        <div class="edit-card">

            <h1>Edit Gift Exchange Session</h1>

            <!-- SESSION EDIT FORM -->
            <form method="post"
                  action="/editSession/${session.getId()}">

                <div class="mb-4">

                    <label class="form-label">
                        Session Name
                    </label>

                    <input class="form-control"
                           type="text"
                           name="sessionName"
                           value="${session.getSessionName()}"
                           required>

                </div>

                <div class="d-flex gap-2">

                    <button type="submit"
                            class="btn btn-save">

                        <i class="bi bi-check-circle"></i>
                        Save Changes

                    </button>

                    <a href="/executeSession/${session.getId()}"
                       class="btn btn-secondary">

                        Cancel

                    </a>

                </div>

            </form>


            <hr class="my-4">


            <h3>Registered Participants</h3>

            @if(session.getParticipants() == null ||
            session.getParticipants().isEmpty())

                <p>No participants have registered.</p>

            @else

                <table class="table table-bordered">

                    <thead>

                    <tr>
                        <th>Name</th>
                        <th class="text-center">SMS Opt-In</th>
                        <th class="text-center">Action</th>
                    </tr>

                    </thead>

                    <tbody>

                    @for(var participant : session.getParticipants())

                        <tr>

                            <td>
                                ${participant.getName()}
                            </td>

                            <td class="text-center">

                                @if(participant.isSmsConsent())

                                    <i class="bi bi-check-circle-fill text-success"></i>

                                @else

                                    <i class="bi bi-x-circle-fill text-danger"></i>

                                @endif

                            </td>

                            <td class="text-center">

                                <!-- SEPARATE REMOVE FORM -->
                                <form method="post"
                                      action="/removeParticipant/${session.getId()}/${participant.getId()}">

                                    <button type="submit">

                                        <i class="bi bi-trash"></i>
                                        Remove

                                    </button>

                                </form>

                            </td>

                        </tr>

                    @endfor

                    </tbody>

                </table>

            @endif

        </div>
    </div>
`
)