package com.source.bricks.entry.transcoder


/**
 * Defines Encoder and Decoder combined together
 * */
interface EntryTranscoder<Raw> :
    EntryEncoder<Raw>,
    EntryDecoder<Raw>