@param freddym.webportfolio.Model.User user
@param freddym.webportfolio.Model.Session session

@template.layouts.main(
    title="Execute Gift Exchange",
    content= @`
        <style>
            .executeSession-main {
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

            .participant-list {
                list-style-type: none;
                padding-left: 0;
            }

            .participant-item {
                margin-bottom: 10px;
            }
        </style>

    <div class="executeSession-main">
        <div class="execute-card">

            <h1>
                @if(session.getSessionName() == null || session.getSessionName().isBlank())
                    Gift Exchange Session
                    @else
                    ${session.getSessionName()}
                @endif
            </h1>

            <div class="mb-4">

                <h3>Participant Registration Link</h3>

                <p>
                    Share this page with everyone joining the gift exchange using the following link:
                </p>

                <a href="/registerParticipant/${session.getId()}"
                   target="_blank"
                   class="btn btn-add">

                    <i class="bi bi-person-plus-fill"></i>
                    Open Participant Registration

                </a>
            </div>

            <h2>
                Participants List
            </h2>
            @if(session.getParticipants() == null || session.getParticipants().isEmpty())

                <div class="alert alert-warning mt-3">
                    <i class="bi bi-exclamation-circle"></i>
                    No participants have registered for this session.
                </div>

            @else

                <div class="table-responsive">

                    <table class="table table-bordered table-hover participant-table">

                        <thead>
                        <tr>
                            <th>Participant Name</th>
                            <th class="text-center">Opted In</th>
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

                                        <i class="bi bi-check-circle-fill text-success opt-in-icon"
                                           title="Opted in"></i>

                                    @else

                                        <i class="bi bi-x-circle-fill text-danger opt-in-icon"
                                           title="Not opted in"></i>

                                    @endif

                                </td>

                            </tr>

                        @endfor

                        </tbody>

                    </table>

                </div>

            @endif
            </div>


        <div class="action-buttons">

            <a href="/editSession/${session.getId()}"
               class="btn btn-edit">

                <i class="bi bi-pencil-fill"></i>
                Edit Session

            </a>

            @if(session.isDeliveryComplete())

                <div class="alert alert-success mb-0">
                    <i class="bi bi-check-circle-fill"></i>
                    This gift exchange has already been executed.
                </div>

            @else
                @if(session.isExecuted())

                <div class="alert alert-warning mb-0">
                    <i class="bi bi-exclamation-circle"></i>
                    Some assignments are still pending delivery. Retry to send only the remaining messages.
                </div>

                <form method="post"
                      action="/executeSession/${session.getId()}">

                    <button type="submit" class="btn btn-execute">
                        <i class="bi bi-arrow-repeat"></i>
                        Retry Pending Messages
                    </button>
                </form>

                @else
                    @if(session.getParticipants().stream().anyMatch(participant -> !participant.isSmsConsent()))
                        @for(var participant : session.getParticipants())
                            @if(!participant.isSmsConsent())
                                <div class="alert alert-warning mb-0">
                                    <i class="bi bi-exclamation-circle"></i>
                                    Participant ${participant.getName()} has not opted in for SMS notifications.
                                </div>
                            @endif
                        @endfor
                    @else
                        <form method="post"
                              action="/executeSession/${session.getId()}">

                            <button type="submit" class="btn btn-execute">
                                <i class="bi bi-play-circle-fill"></i>
                                Execute Gift Exchange
                            </button>
                        </form>
                    @endif
                @endif
            @endif

        </div>
        </div>
`
    )
