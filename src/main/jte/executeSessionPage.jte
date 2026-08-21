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
                    Share this page with everyone joining the gift exchange:
                </p>

                <a href="/registerParticipant/${session.getId()}"
                   target="_blank"
                   class="btn btn-add">

                    Open Participant Registration

                </a>

            </div>
            <h2>
                Participants
            </h2>

            @if(session.getParticipants().isEmpty())
                <p>No participants in this session.</p>
                @else
                    <ul class="participant-list">
                        @for(var participant : session.getParticipants())
                            <li class="participant-item">
                                <strong>${participant.getName()}</strong><br>
                                @if(participant.getPhoneNumber() != null)
                                    <br>
                                    ${participant.getPhoneNumber()}
                                @endif
                            </li>
                            @endfor
                    </ul>

            @endif

            <div class="action-buttons">
                <a href="/editSession/${session.getId()}" class="btn btn-edit">
                    <i class="fas fa-pencil-alt"></i>
                    Edit Session
                </a>

                @if(session.isExecuted())
                    <p>
                        This gift exchange has already been executed.
                    </p>
                @else
                    <form method="post" action="/executeSession/${session.getId()}">
                        <button type="submit" class="btn btn-execute">
                            <i class="fas fa-play-circle"></i>
                            Execute Gift Exchange
                        </button>
                    </form>
                @endif
            </div>
        </div>
    </div>
`
    )
