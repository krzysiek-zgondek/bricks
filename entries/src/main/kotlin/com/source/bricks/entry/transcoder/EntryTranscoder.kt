package com.source.bricks.entry.transcoder

interface EntryTranscoder<Raw> :
    EntryEncoder<Raw>,
        EntryDecoder<Raw>